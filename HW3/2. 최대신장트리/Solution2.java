import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution2 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
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

class Solution2 {
	static final int MAX_N = 20000;
	static final int MAX_E = 80000;

	static int N, E;
	static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
	static int Answer;
	static Edge[] EdgeBucket = new Edge[MAX_E];
	static TreeSet[] NodeBucket = new TreeSet[MAX_N + 1];

	public static void main(String[] args) throws Exception {
		/*
		 * 동일 폴더 내의 input2.txt 로부터 데이터를 읽어옵니다. 또한 동일 폴더 내의 output2.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input2.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output2.txt");
		// double start, time;
		// start = System.currentTimeMillis();

		// Answer1 = BellmanFord1(1);

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
			DoKruskal();

			// output2.txt로 답안을 출력합니다.
			pw.println("#" + test_case + " " + Answer);
			/*
			 * 아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때, 출력한 내용이 실제로 파일에 기록되지 않을 수
			 * 있습니다. 따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
			pw.flush();
		}
		// time = (System.currentTimeMillis() - start);
		// System.out.println(time);
		br.close();
		pw.close();
	}

	/**
	 * Kruskal Algorithm을 통해 최대 신장 트리를 구한다. 
	 * 먼저 주어진 데이터는 정렬을 하기 좋지 않기 때문에 Edge Class로 묶어서 초기화한 후 내림차순으로 정렬한다. 
	 * 정렬은 HeapSort를 사용하였고, E개를 정렬하였으므로 수행시간은 O(ElogE)이다. 
	 * 이후 N개의 Make-Set을 한 뒤 E번 루프를 돌면서(kruskal의 while문) 2*E번의 Find-Set(with path compression)을 통해 Set 비교를 한 뒤, 
	 * Set이 다른 경우에 대해 N-1번의 Union이 일어난다. 
	 * Set operation에 대해서는 총 N+2E+N-1번의 Make-Set, Union, Find-Set(with path compression) 중 N번이 Make-Set이다. 
	 * Rank를 이용한 Union과 경로압축을 이용한 Find-Set은 동시에 사용하기 힘드므로
	 * (Path compression 과정에서 path compression이 일어나지 않은 부분의 rank가 훼손될 수 있고, 
	 * 이를 해결하기 위해 모든 자식으로 가는 연결고리를 가지고, 모든 자식들의 rank를 확인하고 업데이트해줘야 하는데 이는 overhead가 크다, 수업시간 질문 나온 사항) 
	 * 경로압축을 이용한 Find-Set만 이용하였고, 이는 실제 수행시간이 트리의 높이변화에 따라 바뀌어 구하기 힘들지만 
	 * rank를 이용한 union과 경로압축을 이용한 Find-Set을 동시에 사용할 경우 O((2E+2N-1)log*N)으로 알려져 있다. 이를 크게 벗어나지는 않는다.
	 * 따라서 Kruskal 알고리즘을 지배하는 부분은 EdgeSort의 O(ElogE)이다.
	 */
	static void DoKruskal() {
		for (int i = 0; i < E; ++i) {
			EdgeBucket[i] = new Edge(U[i], V[i], W[i]);
		}
		EdgeSort();
		for (int i = 1; i <= N; ++i) {
			NodeBucket[i] = new TreeSet();
			NodeBucket[i].Union(NodeBucket[i]);
		}
		for (int i = 0; i < E; ++i) {
			int st = EdgeBucket[i].getStart();
			int ed = EdgeBucket[i].getEnd();
			if (NodeBucket[st].findSetWithPathCompression() != NodeBucket[ed].findSetWithPathCompression()) {
				Answer += EdgeBucket[i].getWeight();
				NodeBucket[ed].findSetWithPathCompression().Union(NodeBucket[st].findSetWithPathCompression());
			}
		}
	}

	static void EdgeSort() {
		for (int i = E / 2; i > 0; --i) {
			percolateDown(i, E);
		}
		for (int i = E; i > 1; --i) {
			Edge tmp = EdgeBucket[0];
			EdgeBucket[0] = EdgeBucket[i - 1];
			EdgeBucket[i - 1] = tmp;
			percolateDown(1, i - 1);
		}
	}

	static void percolateDown(int i, int n) {
		int lChild = 2 * i;
		int rChild = 2 * i + 1;
		if (lChild <= n) {
			if ((rChild <= n) && (EdgeBucket[lChild - 1].getWeight() > EdgeBucket[rChild - 1].getWeight())) {
				lChild = rChild;
			}
			if (EdgeBucket[i - 1].getWeight() > EdgeBucket[lChild - 1].getWeight()) {
				Edge tmp = EdgeBucket[i - 1];
				EdgeBucket[i - 1] = EdgeBucket[lChild - 1];
				EdgeBucket[lChild - 1] = tmp;
				percolateDown(lChild, n);
			}
		}
	}

	static class Edge {
		int start;
		int end;
		int weight;

		public Edge(int st, int ed, int wg) {
			this.start = st;
			this.end = ed;
			this.weight = wg;
		}

		int getStart() {
			return this.start;
		}

		int getEnd() {
			return this.end;
		}

		int getWeight() {
			return this.weight;
		}

	}

	static class TreeSet {
		TreeSet root;

		public TreeSet() {
			this.root = null;
		}

		TreeSet findSetWithPathCompression() {
			if (this.root == this) {
				return this;
			}
			TreeSet findRoot = this.root;
			while (findRoot.findSetWithPathCompression() != findRoot) {
				findRoot = findRoot.findSetWithPathCompression();
			}
			TreeSet arrangeRoot = this.root;
			this.root = findRoot;
			while (arrangeRoot != findRoot) {
				TreeSet newArrangeRoot = arrangeRoot.root;
				arrangeRoot.root = findRoot;
				arrangeRoot = newArrangeRoot;
			}
			return findRoot;
		}

		void Union(TreeSet rootLL) {
			this.root = rootLL;
		}
	}
}
