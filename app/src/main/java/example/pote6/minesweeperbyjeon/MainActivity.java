package example.pote6.minesweeperbyjeon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/**
 * 전성환 개발 100%
 * 갑자기 만들어보고 싶어서 만든 지뢰찾기게임
 * 이중 배열로 행 10 * 열 10의 게임맵을 구성. 총 지뢰수는 10개.
 * 지뢰의 위치는 랜덤으로 생성
 * 각 사각형이 나타내는 숫자는 자신을 제외한 주변 8개 사각형에 포함된 폭탄의 갯수를 표현
 */
public class MainActivity extends AppCompatActivity {

    public static int row = 10; // 행
    public static int col = 10; // 열
    public static int totalMines = 10; // 게임에 출력될 총 지뢰 수
    public static int [][] gameBoard; // 지뢰 게임 판 구성, 행 10 * 열 10 으로 구성된 이중 배열
    public static HashMap<Integer, Integer> minePosition; // 지뢰 위치 값을 저장할 hashmap
    public Button btn_setMine;
    public TextView txt_resultMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_setMine = findViewById(R.id.btn_set_minesweeper);
        txt_resultMine = findViewById(R.id.txt_result_minesweeper);
        btn_setMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * gameBoard와 minePosition의 생성자를 클릭에서 생성한 이유는
                 * 버튼 클릭시 마다 새로운 결과를 만들기 위해서임.
                 */
                gameBoard = new int[row][col];
                minePosition = new HashMap();
                mineMake();
                setPositionNumber();
                txt_resultMine.setText(showGameBoard());
            }
        });

    } // end of onCreate

    /**
     * mineMake 메소드  => 무작위로 10개의 지뢰를 깔아주는 메소드
     *
     * for문으로 총 지뢰 개수 10개까지 생성될때까지  0 ~ 99 까지의 숫자를 랜덤으로 생성한다.
     * hashmap(변수명 minePosition)에 '키 값 = 랜덤 생성 숫자', 'value값 = -1' 로 저장된다.
     * 저장시에 hashmap의 containsKey로 중복된 값이 아닐시 hashmap에 값을 저장한다. 중복값일 경우 --i 하여 다시 값을 도출한다.
     * hashmap에 저장된 값은 int 형태로 출력하고 해당 int값(변수명 position)/10 => '몫의 값= 행 값'으로, int값(변수명 position)%10 => '나머지의 값=열 값'으로 입력하여 해당 위치의 값을 -1, 즉 지뢰로 설정한다.
     */
    public void mineMake() {
        Random random = new Random();

        for(int i = 0; i< totalMines; ++i) {
            int randomNumber = random.nextInt(100); // 랜덤 숫자 생성
            if(!minePosition.containsKey(randomNumber))  // containKey를 이용하여 중복 생성 방지
            {
                minePosition.put(randomNumber, -1);
                Collection<Integer> collection = minePosition.keySet();
                for (Integer integer : collection) {
                    int position = Integer.parseInt(integer.toString());
                    gameBoard[position / 10][position % 10] = -1;    // 지뢰 위치 설정 => '몫의 값 = 행 값' , '나머지 값 = 열 값'
                }
            }else {
                --i;  // 중복된 값일 경우 다시 값을 도출한다.
            }
        }
    } // End of MineCreate 메소드


    /**
     * makeBoard => 각 배열의 값을 지정해주는 메소드, 자신을 제외한 주변 8개 사각형의 값을 지정해준다.
     * 자신의 값 위치를 [0][0]으로 기준을 잡아 행과 열이 각각-1~1 까지 증감하며 주변 8자리의 값을 설정한다.     *
     */

    public void setPositionNumber(){
        for(int rowNumber = 0; rowNumber < row; ++ rowNumber){
            for(int colNumber = 0; colNumber < col; ++ colNumber){
                if(gameBoard[rowNumber][colNumber] == -1){
                    continue;
                }

                int primaryNumber = 0; // 각 위치의 기본 값을 나타내는 변수명, 초기 값은 0으로 설정.

                for(int r=-1 ; r<=1 ; ++r){  // int r = 행의 위치 변동 값
                    for(int c=-1 ; c<=1 ; ++c){ // int c = 열의 위치 변동 값
                        if(r==0 && c==0){
                            continue;  // r과 c의 값이 0일 때 = 현재 자신의 위치 값
                        }
                        if(rowNumber + r >= 0 && colNumber + c >= 0 && rowNumber + r < row && colNumber + c < col){ // 행과 열이 10 x 10 일 때,
                            if(gameBoard[rowNumber+r][colNumber+c] == -1)  // 자신의 위치 주변의 값이 -1이 있으면 기본 값을 1씩 증감한다.
                            {
                                primaryNumber++;
                            }
                        }
                    }
                }
                gameBoard[rowNumber][colNumber] = primaryNumber; // 최종 변동된 값을 해당 배열에 저장한다.
            }
        }
    } // End of makeBoard 메소드



    /**
     * showGameBoard 메소드 => 지뢰찾기 게임 board에 숫자와 지뢰를 출력해 주는 메소드     *
     * 해당 위치의 값(변수명 position_Number)가 -1 이면 '*' 모양의 지뢰 표시로 변경해준다.
     * 그 외 나머지 값은 해당 위치의 값을 숫자로 입력해 준다.
     */
    public String showGameBoard(){
        StringBuilder result = new StringBuilder();

        for(int i = 0; i< row; ++i){
            for(int j = 0; j< col; ++j){
                int positionNumber;

                positionNumber = gameBoard[i][j];
                if(positionNumber == -1){
                    result.append(" ").append("*");
                }else{
                    result.append(" ").append(positionNumber);
                }
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    } // End of showGameBoard 메소드


}
