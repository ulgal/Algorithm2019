import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution1 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution1.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output1.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution1

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution1
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution1   // 0.5초 수행
       timeout 1 java Solution1     // 1초 수행
 */

class Solution1 {

	static final int MAX_N = 1000;
	static final int MAX_E = 100000;
	static final int Div = 100000000; // 1억
	static int N, E;
	static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
	static int[] Answer1 = new int[MAX_N + 1];
	static int[] Answer2 = new int[MAX_N + 1];
	static double start1, start2;
	static double time1, time2;

	static int[][] segregatedList = new int[1001][1024];
	static int[] segregatedListCounter = new int[1001];

	public static void main(String[] args) throws Exception {
		/*
		 * 동일 폴더 내의 input1.txt 로부터 데이터를 읽어옵니다. 또한 동일 폴더 내의 output1.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input1.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output1.txt");

		/*
		 * 10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			 * 각 테스트 케이스를 표준 입력에서 읽어옵니다. 먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다. 그리고 각 i번째 간선의
			 * 시작점의 번호를 U[i], 끝점의 번호를 V[i]에, 간선의 가중치를 W[i]에 읽어들입니다. (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤
			 * N, 1 ≤ V[i] ≤ N)
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

			/* Problem 1-1 */
			start1 = System.currentTimeMillis();

			BellmanFord1();
			// Answer1 = BellmanFord1(1);
			time1 = (System.currentTimeMillis() - start1);

			/* Problem 1-2 */
			start2 = System.currentTimeMillis();
			// Answer2 = Answer1;
			BellmanFord2();
			time2 = (System.currentTimeMillis() - start2);

			// output1.txt로 답안을 출력합니다.
			pw.println("#" + test_case);
			for (int i = 1; i <= N; i++) {
				pw.print(Answer1[i]);
				if (i != N)
					pw.print(" ");
				else
					pw.print("\n");
			}
			pw.println(time1);

			for (int i = 1; i <= N; i++) {
				pw.print(Answer2[i]);
				if (i != N)
					pw.print(" ");
				else
					pw.print("\n");
			}
			pw.println(time2);
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
	 * Basic Bellman-Ford Algorithm 초기화하는데 N의 시간, 첫 루프에서 N(Vertext의 개수), 
	 * 다음 루프에서 E번 돌고 내부에선 상수시간이므로 theta(NE)의 time complex
	 */
	private static void BellmanFord1() {
		Answer1[1] = 0;
		for (int i = 2; i <= N; ++i) {
			Answer1[i] = Div;
		}
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < E; ++j) {
				if (Answer1[U[j]] != Div) {
					Answer1[V[j]] = (Answer1[U[j]] + W[j] < Answer1[V[j]]) ? (Answer1[U[j]] + W[j]) : (Answer1[V[j]]);
				}
			}
		}
		for (int i = 1; i <= N; ++i) {
			Answer1[i] %= Div;
		}
	}

	/**
	 * Advanced Bellman-Ford Algorithm Basic 버전의 문제점은, 매 iterate마다 모든 Edge에 대해 relaxation 가능 여부를 체크해야 한다는 것이다. 
	 * 이를 개선할 방법으로는 relaxation이 일어나는 시점을 생각하면 relaxation이 일어나는 edge에 대해 egde의 시작점이 바로 직전 iterate에서 relaxation된 경우에만 일어난다.
	 * 따라서 relaxation된 edge들을 모아서 관리하고, 모인 edge들에 대해 다시 relaxation을 진행하는 방법을 N번 반복한다면 비효율을 줄일 수 있다. 
	 * 이렇게 하기 위해선 현재 수행하는 리스트와, 다음 단계에 수행할 리스트 두 가지를 가지고 있어야 하며, 매 반복이 끝나면 다음 단계에 수행할 리스트를 복사해야 하는 비효율이 발생한다. 
	 * 하나의 리스트를 가지고 relaxation이 일어나는 node를 지속적으로 리스트 뒤에 추가해주는 방식을 사용한다면 이러한 비효율을 없앨 수 있다. 
	 * 마지막으로 남는 비효율은 개념 상 같은 단계에서 하나의 노드가 두 번 이상 relaxation이 일어나는 경우 list에 중복이 생기는 것인데, 
	 * 이를 위해 정렬 및 중복체크를 하는 것이 더 큰 비효율을 발생시킬 수 있어 이 점은 무시하고 구현했다. 
	 * 현재 Edge들을 저장하고있는 자료 구조가 독립된 Array의 index로 iterate하므로 효율적 사용에 적합하지 않다. 
	 * 하지만 이를 위해 Edge라는 static class를 만들고 Edge들을 따로 관리하는 것은 추가적인 연산이 많이 들어가므로 단순히 시간 최적화의 목적으로는 적합하지 않다. 
	 * 최대 1000개의 Node 조건과 http://etl.snu.ac.kr/mod/ubboard/article.php?id=856254&bwid=1860614 에 따라 하나의 Node는 최대 1000개의 Edge를 가질 수 있으므로,
	 * segregatedList와 segregatedListCounter 배열을 Solution1 Class의 static 변수로 선언하였다. 
	 * segregatedList[i][x]는 i번째 노드가 시작점인 Edge의 U[], V[], W[] 배열에서의 index를 갖고, 
	 * segragatedListCount[i]는 앞의 2차원 배열의 x에 해당하는 index가 최대 몇 까지 갈 수 있는지에 대한 정보를 갖는다. 
	 * 이와 더불어 전부 체크하던 기존 방식에서 relaxation 된 노드의 주변만 살피는 방식으로 바꾸기 위해 체크할 노드들을 갖는 LinkedList를 선언하였다. 
	 * 초기 노드 거리 설정 및 선언한 segregatedList 초기화하는데 N+E의 시간이 들고, 이후 relaxation만큼의 시간이 든다. 
	 * relaxation은 만약의 경우 모든 edge 체크 마다 relaxation이 일어난다면 O(NE + N + E) = O(NE)의 시간이 들지만 실제로는 그래프의 크기가 커질수록 이럴 가능성은 매우 희박해지며 
	 * relaxation이 일어나는 경로는 그래프의 모양, 체크하는 순서에 따라 다르며 이는주어진 input값을 비교할 때 3배에서 20배 가량의 variation이 있음을 통해 알 수 있다. 
	 * 따라서 구할 수 있는 time complex는 상한인 O(NE)이다.
	 */

	private static void BellmanFord2() {
		int i = 0, j = 0, cmpTarget = 0;
		Answer2[1] = segregatedListCounter[1] = 0;
		for (i = 2; i <= N; ++i) {
			Answer2[i] = Div;
			segregatedListCounter[i] = 0;
		}
		for (i = 0; i < E; ++i) {
			segregatedList[U[i]][segregatedListCounter[U[i]]++] = i;
		}
		LinkedList targetQueue = new LinkedList(1);
		LinkedList tail = targetQueue;
		while (targetQueue != null) {
			for (j = 0; j < segregatedListCounter[targetQueue.getTarget()]; ++j) {
				cmpTarget = segregatedList[targetQueue.getTarget()][j];
				if (Answer2[U[cmpTarget]] + W[cmpTarget] < Answer2[V[cmpTarget]]) {
					Answer2[V[cmpTarget]] = Answer2[U[cmpTarget]] + W[cmpTarget];
					LinkedList nextTarget = new LinkedList(V[cmpTarget]);
					tail.setNext(nextTarget);
					tail = nextTarget;
				}
			}
			targetQueue = targetQueue.getNext();
		}
		for (i = 1; i <= N; ++i) {
			Answer2[i] %= Div;
		}
	}

	static class LinkedList {
		int target;
		LinkedList next;

		public LinkedList() {
			this.next = null;
		}

		public LinkedList(int tg) {
			this.target = tg;
			this.next = null;
		}

		boolean hasNext() {
			return this.next != null;
		}

		LinkedList getNext() {
			return this.next;
		}

		void setNext(LinkedList nextLL) {
			this.next = nextLL;
		}

		int getTarget() {
			return this.target;
		}

		void setTarget(int val) {
			this.target = val;
		}
	}
}
