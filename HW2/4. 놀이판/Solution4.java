import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution4.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output4.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution4

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution4
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution4   // 0.5초 수행
       timeout 1 java Solution4     // 1초 수행
 */

class Solution4 {
	static final int max_n = 100000;

	static int n;
	static int[][] A = new int[3][max_n];
	static int Answer;
	static int[] TableA = new int[max_n];
	static int[] TableB = new int[max_n];
	static int[] TableC = new int[max_n];
	static int[] TableD = new int[max_n];
	static int[] TableE = new int[max_n];
	static int[] TableF = new int[max_n];

	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input4.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output4.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input4.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output4.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 놀이판의 열의 개수를 n에 읽어들입니다.
			   그리고 첫 번째 행에 쓰여진 n개의 숫자를 차례로 A[0][0], A[0][1], ... , A[0][n-1]에 읽어들입니다.
			   마찬가지로 두 번째 행에 쓰여진 n개의 숫자를 차례로 A[1][0], A[1][1], ... , A[1][n-1]에 읽어들이고,
			   세 번째 행에 쓰여진 n개의 숫자를 차례로 A[2][0], A[2][1], ... , A[2][n-1]에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken());
			for (int i = 0; i < 3; i++) {
				stk = new StringTokenizer(br.readLine());
				for (int j = 0; j < n; j++) {
					A[i][j] = Integer.parseInt(stk.nextToken());
				}
			}


			/////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
			/////////////////////////////////////////////////////////////////////////////////////////////
			Answer = maxScore();


			// output4.txt로 답안을 출력합니다.
			pw.println("#" + test_case + " " + Answer);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
			pw.flush();
		}

		br.close();
		pw.close();
	}

	/**
	 * () = 세모
	 * TableA = O, X, ()	//	TableB = O, (), X	//	TableC = X, O, ()	//	TableD = (), O, X	//	TableE = X, (), O	//	TableF = (), X, O
	 * 양립가능한 조합은 A, D, E	//	B, C, F
	 * 이에 맞게 Table*[0]들을 초기화시킨 후 max(양립가능한 두 Table의 이전state) + 해당 열의 연산값으로 해당 Table의 state를 할당
	 * 수행시간을 지배하는 연산은 for문이므로 T(n) = theta(n)
	 */
	private static int maxScore(){
		int i = 0, max_score, max_score_1, max_score_2, max_score_3;
		TableA[0] = A[0][0] - A[1][0];
		TableB[0] = A[0][0] - A[2][0];
		TableC[0] = A[1][0] - A[0][0];
		TableD[0] = A[1][0] - A[2][0];
		TableE[0] = A[2][0] - A[0][0];
		TableF[0] = A[2][0] - A[1][0];
		for(i = 1; i < n; ++i){
			TableA[i] = A[0][i] - A[1][i] + (( TableD[i-1]>TableE[i-1] ) ? ( TableD[i-1] ) : ( TableE[i-1] ));
			TableB[i] = A[0][i] - A[2][i] + (( TableC[i-1]>TableF[i-1] ) ? ( TableC[i-1] ) : ( TableF[i-1] ));
			TableC[i] = A[1][i] - A[0][i] + (( TableB[i-1]>TableF[i-1] ) ? ( TableB[i-1] ) : ( TableF[i-1] ));
			TableD[i] = A[1][i] - A[2][i] + (( TableA[i-1]>TableE[i-1] ) ? ( TableA[i-1] ) : ( TableE[i-1] ));
			TableE[i] = A[2][i] - A[0][i] + (( TableA[i-1]>TableD[i-1] ) ? ( TableA[i-1] ) : ( TableD[i-1] ));
			TableF[i] = A[2][i] - A[1][i] + (( TableB[i-1]>TableC[i-1] ) ? ( TableB[i-1] ) : ( TableC[i-1] ));
		}
		max_score_1 = ((TableA[n-1] > TableB[n-1]) ? (TableA[n-1]) : (TableB[n-1]));
		max_score_2 = ((TableC[n-1] > TableD[n-1]) ? (TableC[n-1]) : (TableD[n-1]));
		max_score_3 = ((TableE[n-1] > TableF[n-1]) ? (TableE[n-1]) : (TableF[n-1]));
		max_score = max_score_1 > max_score_2 ? max_score_1 : max_score_2;
		max_score = max_score > max_score_3 ? max_score : max_score_3;
		return max_score;
	}
}

