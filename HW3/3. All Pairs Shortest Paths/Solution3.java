import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution3 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution3.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output3.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution3

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution3
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution3   // 0.5초 수행
       timeout 1 java Solution3     // 1초 수행
 */

class Solution3 {
	static final int MAX_N = 200;
	static final int MAX_E = 10000;
	static final int INFINITY = 1000 * 200 + 1;
	static int N, E;
	static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
	static int Answer;
	static int[][] D = new int[MAX_N + 1][MAX_N + 1];
	public static void main(String[] args) throws Exception {
		/*
		 * 동일 폴더 내의 input3.txt 로부터 데이터를 읽어옵니다. 또한 동일 폴더 내의 output3.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input3.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output3.txt");

		/*
		 * 10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			 * 각 테스트 케이스를 표준 입력에서 읽어옵니다. 먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다. 그리고 각 i번째 간선의 양
			 * 끝점의 번호를 U[i], V[i]에 읽어들이고, i번째 간선의 가중치를 W[i]에 읽어들입니다. (0 ≤ i ≤ E-1, 1 ≤ U[i]
			 * ≤ N, 1 ≤ V[i] ≤ N)
			 */
			stk = new StringTokenizer(br.readLine());
			N = Integer.parseInt(stk.nextToken());
			E = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < E; i++) {
				U[i] = Integer.parseInt(stk.nextToken());
				V[i] = Integer.parseInt(stk.nextToken());
				W[i] = Integer.parseInt(stk.nextToken());
			}

			/////////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * 이 부분에서 여러분의 알고리즘이 수행됩니다. 문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
			/////////////////////////////////////////////////////////////////////////////////////////////
			Answer = 0;
			FloydWarshall();

			// output3.txt로 답안을 출력합니다.
			pw.println("#" + test_case + " " + Answer);
			/*
			 * 아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때, 출력한 내용이 실제로 파일에 기록되지 않을 수
			 * 있습니다. 따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
			pw.flush();
		}

		br.close();
		pw.close();
	}

	/**
	 * N*N 배열을 INFINITY로 채운다(주어진 조건에서 구할 수 있는 최댓값인 200000+1), theta(N*N).
	 * 이후 E개의 edge들을 배열에 넣는다. 중복을 고려하여 기존 배열의 값과 weight의 값을 비교한다, theta(E).(algorithm에선 theta(V*V)인 부분) 
	 * 이후 N번 N*N 배열을 순회하여 floyd-warshall algorithm을 수행한다, theta(N*N*N). 
	 * 마지막으로 N*N 배열을 순회하며 Answer를 업데이트한다, theta(N*N).
	 * dominant한 수행시간은 theta(N*N*N)이다.
	 */
	static void FloydWarshall() {
		for (int i = 1; i <= N; ++i) {
			for (int j = 1; j <= N; ++j) {
				if (i != j) {
					D[i][j] = INFINITY;
				}
			}
		}
		for (int i = 0; i < E; ++i) {
			D[U[i]][V[i]] = (W[i] < D[U[i]][V[i]]) ? (W[i]) : (D[U[i]][V[i]]);
		}
		for (int k = 1; k <= N; ++k) {
			for (int i = 1; i <= N; ++i) {
				for (int j = 1; j <= N; ++j) {
					D[i][j] = (D[i][j] < D[i][k] + D[k][j]) ? (D[i][j]) : (D[i][k] + D[k][j]);
				}
			}
		}

		for (int i = 1; i <= N; ++i) {
			for (int j = 1; j <= N; ++j) {
				if (i != j) {
					Answer += D[i][j];
				}
			}
		}
	}
}
