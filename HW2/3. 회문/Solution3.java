import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
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

	static int n;
	static String s;
	static int Answer;

	static int[][] LCSTable = new int[5001][5001];

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
			 * 각 테스트 케이스를 파일에서 읽어옵니다. 첫 번째 행에 쓰여진 문자열의 길이를 n에 읽어들입니다. 그 다음 행에 쓰여진 문자열을 s에
			 * 한번에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken());
			s = br.readLine();

			/////////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * 이 부분에서 여러분의 알고리즘이 수행됩니다. 문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
			/////////////////////////////////////////////////////////////////////////////////////////////
			Answer = maxCommonSuborder();

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
	 * 입력받은 String str과 str의 역순 revStr에 대해서 최장 공통 부분순서를 구하는 방식으로 문제를 해결
	 * LCSTable[i][j] 에 대해서 i는 string의 index, j는 revStr의 index라 할 때,
	 * str.charAt(i-1)과 revStr.charAt(j-1)이 같으면 LCSTable[i][j] = LCSTable[i-1][j-1]
	 * + 1 다르면 LCSTable[i][j] = max(LCSTable[i-1][j], LCSTable[i][j-1])이다. 이와 같은
	 * 방식으로 table을 채워 i+j의 합이 n인 부분들에서의 최댓값을 2배 하면 최장 회문의 값을 구할 수 있다. 이를 구현하는 것은 어렵지
	 * 않지만 이렇게 최장 공통 부분순서를 구하는 방식으로 최장 회문을 구하면 항상 가장 긴 회문의 값이 짝수가 나오므로, 이에 대한 처리를
	 * 해줘야 한다. 가장 긴 회문의 값이 홀수가 나오는 조건으로는 i+j의 합이 n일 때 LCSTable[i-1][j]와
	 * LCSTable[i][j-1], LCSTable[i][j]의 관계를 통해 구할 수 있는데, 만약 i+j = n인 상황에서
	 * LCSTable[i][j]의 값과LCSTable[i-1][j] 또는 LCSTable[i][j-1]의 값이 같다면 가장 끝에 붙어있는 문자가
	 * 없어져도 최장공통부분순서는 같다는 의미이므로 없어져도 되는 문자가 회문 가운데 들어갈 수 있어 홀수 개가 가능하다. 이러한 방식으로 구하면
	 * maxCommonSuborder에서 dominate한 부분은 2중포문이므로 T(n) = theta(n^2)이다.
	 */

	private static int maxCommonSuborder() {

		String revS = (new StringBuffer(s)).reverse().toString();
		int i, j, max = 0;
		boolean oddFlag = false;
		for (i = 0; i < n; ++i) {
			LCSTable[0][i] = 0;
			LCSTable[i][0] = 0;
		}
		for (i = 1; i < n; ++i) {
			for (j = 1; j < n - i + 1; ++j) {
				if (revS.charAt(i - 1) == s.charAt(j - 1)) {
					LCSTable[i][j] = LCSTable[i - 1][j - 1] + 1;
				} else {
					LCSTable[i][j] = (LCSTable[i - 1][j] > LCSTable[i][j - 1]) ? (LCSTable[i - 1][j]) : (LCSTable[i][j - 1]);
				}
			}

		}
		for (i = 1; i < n; ++i) {
			if ((LCSTable[i][n - i] == LCSTable[i - 1][n - i] || LCSTable[i][n - i] == LCSTable[i][n - i - 1])) {
				if ((LCSTable[i][n - i] + 1) > max) {
					max = LCSTable[i][n - i] + 1;
					oddFlag = true;
				}
			} else {
				if (LCSTable[i][n - i] >= max) {
					max = LCSTable[i][n - i];
					oddFlag = false;
				}
			}
		}
		return (oddFlag) ? (max * 2 - 1) : (max * 2);
	}
}
