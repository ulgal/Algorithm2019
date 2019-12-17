import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution2.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output2.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution2

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution2
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution2   // 0.5초 수행
       timeout 1 java Solution2     // 1초 수행
 */

class Solution2_another {

	/*
	 ** 주의사항 정답의 숫자가 매우 크기 때문에 답안은 반드시 int 대신 long 타입을 사용합니다. 그렇지 않으면 overflow가 발생해서
	 * 0점 처리가 됩니다. Answer[0]을 a의 개수, Answer[1]을 b의 개수, Answer[2]를 c의 개수라고 가정했습니다.
	 */
	static int n; // 문자열 길이
	static String s; // 문자열
	static long[] Answer = new long[3]; // 정답
	static long[][] BucketA = new long[30][30];
	static long[][] BucketB = new long[30][30];
	static long[][] BucketC = new long[30][30];

	public static void main(String[] args) throws Exception {
		/*
		 * 동일 폴더 내의 input2.txt 로부터 데이터를 읽어옵니다. 또한 동일 폴더 내의 output2.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input2.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output2_1.txt");

		/*
		 * 10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			 * 각 테스트 케이스를 파일에서 읽어옵니다. 첫 번째 행에 쓰여진 문자열의 길이를 n에 읽어들입니다. 그 다음 행에 쓰여진 문자열을 s에
			 * 한번에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken());
			s = br.readLine();
			calculate();
			/////////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * 이 부분에서 여러분의 알고리즘이 수행됩니다. 정답을 구하기 위해 주어진 문자열 s를 여러분이 원하시는 대로 가공하셔도 좋습니다. 문제의
			 * 답을 계산하여 그 값을 Answer(long 타입!!)에 저장하는 것을 가정하였습니다.
			 */
			/////////////////////////////////////////////////////////////////////////////////////////////
			Answer[0] = BucketA[0][n - 1]; // a 의 갯수
			Answer[1] = BucketB[0][n - 1]; // b 의 갯수
			Answer[2] = BucketC[0][n - 1]; // c 의 갯수

			// output2.txt로 답안을 출력합니다.
			pw.println("#" + test_case + " " + Answer[0] + " " + Answer[1] + " " + Answer[2]);
			/*
			 * 아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때, 출력한 내용이 실제로 파일에 기록되지 않을 수
			 * 있습니다. 따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
			pw.flush();
		}

		br.close();
		pw.close();
	}

	/*
	 * 기본적인 아이디어는 2차원 배열 세 개를 선언하여, 관계를 이용하여 배열에 저장한다. Bucket*[length-1][startIndex]
	 * // * = A | B | C, length = string 길이, startIndex = string 상에서 시작하는 지점
	 * BucketA[1][1]은 string 상의 index 1부터 시작해서 길이가 2인 부분string이 A가 될 수 있는 경우의 수이고,
	 * BucketA[3][4]는 string 상의 index 4부터 시작해서 길이가 4인 부분string이 A가 될 수 있는 경우의 수이다.
	 * 시간복잡도는 초기에 길이가 1인 부분, 즉 각 index별로 배열에 넣어주는 시간 theta(n) 이후 i <- 1 to n, j <- 0
	 * to n-i를 통해 [i][j]에 접근하는 시간은 theta(n^2) 접근한 뒤 계산하기 위해 각각 k < 0 to i는
	 * theta(n)이므로 총 시간복잡도 T(n) = theta(n) + theta(n^2) * theta(n) = theta(n^3) n^3에
	 * 비례한 시간 복잡도를 갖는다.
	 */
	private static void calculate() {
		int i, j, k;
		for (i = 0; i < n; ++i) {
			switch (s.charAt(i)) {
			case 'a':
				BucketA[i][i] = 1;
				BucketB[i][i] = BucketC[i][i] = 0;
				break;
			case 'b':
				BucketB[i][i] = 1;
				BucketA[i][i] = BucketC[i][i] = 0;
				break;
			case 'c':
				BucketC[i][i] = 1;
				BucketA[i][i] = BucketB[i][i] = 0;
				break;
			}
		}
		for (j = 1; j < n; ++j) {
			for (i = j - 1; i > -1; --i) {
				BucketA[i][j] = BucketB[i][j] = BucketC[i][j] = 0;
				for (k = i; k < j; ++k) {
					BucketA[i][j] += (BucketA[i][k] * BucketC[k + 1][j]) + (BucketB[i][k] * BucketC[k + 1][j])
							+ (BucketC[i][k] * BucketA[k + 1][j]);
					BucketB[i][j] += (BucketA[i][k] * BucketA[k + 1][j]) + (BucketA[i][k] * BucketB[k + 1][j])
							+ (BucketB[i][k] * BucketB[k + 1][j]);
					BucketC[i][j] += (BucketB[i][k] * BucketA[k + 1][j]) + (BucketC[i][k] * BucketB[k + 1][j])
							+ (BucketC[i][k] * BucketC[k + 1][j]);
				}
			}
		}
	}
}
