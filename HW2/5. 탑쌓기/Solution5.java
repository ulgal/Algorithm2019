import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;
/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution5 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution5.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output5.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution5

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution5
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution5   // 0.5초 수행
       timeout 1 java Solution5     // 1초 수행
 */

class Solution5 {
	static final int max_n = 1000;

	static int n, H;
	static int[] h = new int[max_n], d = new int[max_n-1];
	static int Answer;
	static int[][] possibleTable = new int[max_n][10001];
	static int[][] sumBeforeTwoTable = new int[max_n-2][10001];

	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input5.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output5.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input5.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output5.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 블록의 개수와 최대 높이를 각각 n, H에 읽어들입니다.
			   그리고 각 블록의 높이를 h[0], h[1], ... , h[n-1]에 읽어들입니다.
			   다음 각 블록에 파인 구멍의 깊이를 d[0], d[1], ... , d[n-2]에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken()); H = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n; i++) {
				h[i] = Integer.parseInt(stk.nextToken());
			}
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n-1; i++) {
				d[i] = Integer.parseInt(stk.nextToken());
			}


			/////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
			/////////////////////////////////////////////////////////////////////////////////////////////
			Answer = find(n, H);


			// output5.txt로 답안을 출력합니다.
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
	 * Algorithm
	 * 재귀적 구조를 살펴보면 가장 위 블록부터 아래로 내려가고, maxH = (제한 H)-(현재 탑의 높이)라고 할 때
	 * k번째 블록이 가장 아래있는 경우의 수는 
	 * 		k+1번째 블록이 가장 아래있고 그 탑의 높이와 h[k]-d[k]를 더한값이 H보다 작거나 같은 것 +
	 * 		k+2번째 블록이 가장 아래있고 그 탑의 높이와 h[k]를 더한 값이 H보다 작거나 같은 것 +
	 * 		...
	 * 		n번째 블록이 가장 아래있고 그 탑의 높이와 h[k]를 더한 값이 H보다 작거나 같은 것이다.
	 * 이를 가장 위 블록부터 아래로 채워나가면 최악의 경우 시간복잡도는 n*H의 table을 채우고 그 안에서 최대 n번의 계산이 일어나므로
	 * O((n^2)*H)이고, 주어진 조건 하에서 하나의 문제에 대해 최대 10000000000 * k 번의 연산이 이루어지게 된다.
	 * 일반적인 컴퓨터가 1초에 할 수 있는 연산이 억 단위이므로, 5개 이상의 100억 단위의 문제를 1초 내에 해결하기엔 턱없이 부족하다.
	 * 따라서 sumBeforeTwoTable이라는 Table을 선언하고, 
	 * sumBeforeTwoTable[k][h] = 탑의 높이가 h, 가장 아래 블록이 k+2번째 블록인 경우의 수 부터 n번째 블록인 경우의 수까지 모두 더한 값이라 할 때
	 * sumBeforeTwoTable[k-1][h] = sumBeforeTwoTable[k][h] + possibleTable[k+1][h]
	 * 위와 같은 식이 성립하여 내부에서 일어나는 O(n)을 O(1)로 단축시켰다.
	 * 이를 이용하면 O(n*H)번 실행이 일어난다.
	 * 사용한 메모리는 (((1000*10001 + 998*10001 + 1000 + 999) * 4) / 10^6) = 79.9MB로 제한 메모리 요구 조건을 만족한다.
	 */
	private static int find(int idx, int maxH){
		int i, j;
		for(i=maxH; i>=h[idx-1]; --i){
			possibleTable[idx-1][i] = 1;
			sumBeforeTwoTable[idx-3][i] = 1;
		}
		for(i=h[idx-1]-1; i>=0; --i){
			possibleTable[idx-1][i] = 0;
			sumBeforeTwoTable[idx-3][i] = 0;
		}
		if(h[idx-1]+h[idx-2]-d[idx-2] > h[idx-2]){
			for(i=maxH; i>=h[idx-1]+h[idx-2]-d[idx-2]; --i){
				possibleTable[idx-2][i] = 2;
			}
			for(i=h[idx-1]+h[idx-2]-d[idx-2] - 1; i>=h[idx-2]; --i){
				possibleTable[idx-2][i] = 1;
			}
			for(i=h[idx-2]-1; i>=0; --i){
				possibleTable[idx-2][i] = 0;
			}
		}
		else{
			for(i=maxH; i>=h[idx-2]; --i){
				possibleTable[idx-2][i] = 2;
			}
			for(i=h[idx-2] - 1; i>=h[idx-1]+h[idx-2]-d[idx-2]; --i){
				possibleTable[idx-2][i] = 1;
			}
			for(i=h[idx-2]-1; i>=0; --i){
				possibleTable[idx-2][i] = 0;
			}
		}
		for(i= idx-3; i>=0; --i){
			for(j=maxH; j>=h[i]; --j){
				possibleTable[i][j] = 1 + (possibleTable[i+1][j-(h[i]-d[i])]);
				possibleTable[i][j] += sumBeforeTwoTable[i][j-h[i]];
				possibleTable[i][j] %= 1000000;
				if(i>0){
					sumBeforeTwoTable[i-1][j] = (sumBeforeTwoTable[i][j] + possibleTable[i+1][j]) % 1000000;
				}
			}
			for(j=h[i]-1; j>=h[i]-d[i]; --j){
				possibleTable[i][j] = possibleTable[i+1][j-(h[i]-d[i])];
				if(i>0){
					sumBeforeTwoTable[i-1][j] = (sumBeforeTwoTable[i][j] + possibleTable[i+1][j]) % 1000000;
				}
			}
			for(j=h[i]-d[i]-1; j>=0; --j){
				possibleTable[i][j] = 0;
				if(i>0){
					sumBeforeTwoTable[i-1][j] = (sumBeforeTwoTable[i][j] + possibleTable[i+1][j]) % 1000000;
				}
			}
		}
		int ans = (sumBeforeTwoTable[0][maxH] + possibleTable[0][maxH] + possibleTable[1][maxH]) % 1000000;
		return ans;
	}
}