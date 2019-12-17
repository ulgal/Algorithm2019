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
	static final int max_n = 1000000;
	static int[] A = new int[max_n];
	static int[] copied = new int[max_n];
	static int n;
	static int Answer1, Answer2, Answer3;
	static long start;
	static double time1, time2, time3;

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
			 * 각 테스트 케이스를 표준 입력에서 읽어옵니다. 먼저 배열의 원소의 개수를 n에 읽어들입니다. 그리고 각 원소를 A[0], A[1], ...
			 * , A[n-1]에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n; i++) {
				A[i] = Integer.parseInt(stk.nextToken());
			}

			/////////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * 이 부분에서 여러분의 알고리즘이 수행됩니다. 원본으로 주어진 입력 배열이 변경되는 것을 방지하기 위해, 여러분이 구현한 각각의 함수에
			 * 복사한 배열을 입력으로 넣도록 코드가 작성되었습니다.
			 * 
			 * 문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다. 함수를 구현하면 Answer1, Answer2, Answer3에
			 * 해당하는 주석을 제거하고 제출하세요.
			 * 
			 * 문제 1은 java 프로그래밍 연습을 위한 과제입니다.
			 */

			/* Problem 1-1 */
			System.arraycopy(A, 0, copied, 0, n);
			start = System.currentTimeMillis();
			Answer1 = merge1(copied);
			time1 = (System.currentTimeMillis() - start) / 1000.;

			/* Problem 1-2 */
			System.arraycopy(A, 0, copied, 0, n);
			start = System.currentTimeMillis();
			Answer2 = merge2(copied);
			time2 = (System.currentTimeMillis() - start) / 1000.;

			/* Problem 1-3 */
			System.arraycopy(A, 0, copied, 0, n);
			start = System.currentTimeMillis();
			Answer3 = merge3(copied);
			time3 = (System.currentTimeMillis() - start) / 1000.;

			/*
			 * 여러분의 답안 Answer1, Answer2, Answer3을 비교하는 코드를 아래에 작성. 세 개 답안이 동일하다면
			 * System.out.println("YES"); 만일 어느하나라도 답안이 다르다면 System.out.println("NO");
			 */
			if (Answer1 == Answer2 && Answer1 == Answer3) {
				System.out.println("YES");
			} else {
				System.out.println("NO");

			}
			/////////////////////////////////////////////////////////////////////////////////////////////

			// output1.txt로 답안을 출력합니다. Answer1, Answer2, Answer3 중 구현된 함수의 답안 출력
			pw.println("#" + test_case + " " + Answer3 + " " + time1 + " " + time2 + " " + time3);
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
	 * MergeSort1은 일반적으로 많이 사용하는 2개로 나누는 Merge Sort이다.
	 * T(n) = mergeSort1(arr, n), Q(n) = doMerge1(arr1, arr2){arr1.length + arr2.length = n} 일 때
	 * T(n) = 2*T(n/2) + Q(n) + theta(n)
	 * 으로 나타낼 수 있고, 이 때 theta(n)은 System.arraycopy를 두번 call하여 총 n개의 element를 array에 단순 copy 하기 때문에 theta(n)으로 나타낼 수 있으며, 
	 * arr1과 arr2 할당, i값 할당 및 계산 등은 상수 시간으로  theta(n)에 포함된다.
	 * Q(n)은 best case는 정렬된 한쪽 배열의 모든 값이 정렬된 다른쪽 배열의 첫 값보다 작은 경우로, 이 경우 n/2번 비교 후 삽입, n/2번 단순 삽입이고
	 * 식으로 나타내면 Q(n) = 6*(n/2) + 4*(n/2) + 1 
	 * worst case는 마지막 원소를 제외하고 계속 비교가 일어나는, 번갈아서 삽입되는 경우로, 이 경우 n-1번 비교 후 삽입, 1번 단순삽입이다. 
	 * 식으로 나타내면 Q(n) = 6*(n-1) + 5
	 * best case와 worst case 모두 theta(n)이므로, 정리하면 식은
	 * T(n) = 2*T(n/2) + theta(n)이다.
	 * f(n) = theta(n), h(n) = n으로 f(n)/h(n) = theta(1)이므로, 마스터 정리에 의해 T(n) = theta(nlogn)이다.
	 */

	private static int merge1(int[] arr) {
		arr = mergeSort1(arr, n);
		int retVal = 0;
		for (int i = 0; i < n; i++) {
			if (i % 4 == 0) {
				retVal += arr[i] % 7;
			}
		}
		return retVal;
	}

	private static int[] mergeSort1(int[] arr, int length) {
		if (length > 1) {
			int i = length / 2;
			int[] arr1 = new int[i];
			System.arraycopy(arr, 0, arr1, 0, i);
			int[] arr2 = new int[length - i];
			System.arraycopy(arr, i, arr2, 0, length - i);
			arr1 = mergeSort1(arr1, i);
			arr2 = mergeSort1(arr2, length - i);
			arr = doMerge1(arr1, arr2);
		}
		return arr;
	}

	private static int[] doMerge1(int[] arr1, int[] arr2) {
		int indexArr1 = 0;
		int indexArr2 = 0;
		int retArrIndex = 0;
		int[] retArr = new int[arr1.length + arr2.length];
		while (indexArr1 < arr1.length || indexArr2 < arr2.length) {
			if (indexArr1 >= arr1.length) {
				while (indexArr2 < arr2.length) {
					retArr[retArrIndex] = arr2[indexArr2];
					retArrIndex++;
					indexArr2++;
				}
			} else if (indexArr2 >= arr2.length) {
				while (indexArr1 < arr1.length) {
					retArr[retArrIndex] = arr1[indexArr1];
					retArrIndex++;
					indexArr1++;
				}
			} else {
				if (arr1[indexArr1] < arr2[indexArr2]) {
					retArr[retArrIndex] = arr1[indexArr1];
					indexArr1++;
				} else {
					retArr[retArrIndex] = arr2[indexArr2];
					indexArr2++;
				}
				retArrIndex++;
			}
		}
		return retArr;
	}

	/*
	 * MergeSort2는 단순히 16개로 나누는 Merge Sort이다.
	 * T(n) = mergeSort1(arr, n), Q(n) = doMerge1(arr[], Exist[]){arrEntry의 총 원소의 합 = n} 일 때
	 * T(n) = 16*T(n/16) + Q(n) + theta(n)
	 * 으로 나타낼 수 있고, 이 때 theta(n)은 System.arraycopy를 16번 call하여 총 n개의 element를 array에 단순 copy 하기 때문에 theta(n)으로 나타낼 수 있으며, 
	 * 16번의 arrEntry 할당, isExsit 할당 및 start 값 계산 등은 상수 시간으로  theta(n)에 포함된다.
	 * Q(n)은 best case는 정렬된 한쪽 배열의 모든 값이 정렬된 다른쪽 배열의 첫 값보다 작은 경우가 recursive하게 15번 나타나는 경우로, 
	 * 이 경우 16번 비교 후 삽입이 n-16회, 단순 삽입 16회이고 while문의 종료조건을 위한 isExist 체크가 16*n번 돈다.
	 * 식으로 나타내면 Q(n) = 16*n + (16+...+2)*(n/16) + 16
	 * worst case는 마지막 원소를 제외하고 계속 비교가 일어나는, 번갈아서 삽입되는 경우로, 이 경우 n-16번 비교 후 삽입, 1번 단순삽입이며, 
	 * 마찬가지로 while문의 종료조건을 위한 isExist 체크가 16*n번 돈다.
	 * 식으로 나타내면 Q(n) = 16*n + 16*(n-16) + 16
	 * best case와 worst case 모두 theta(n)이므로, 정리하면 식은
	 * T(n) = 16*T(n/16) + theta(n)이다.
	 * f(n) = theta(n), h(n) = n으로 f(n)/h(n) = theta(1)이므로, 마스터 정리에 의해 T(n) = theta(nlogn)이다.
	 * 다만 이 경우 16보다 작은 경우도 일괄적으로 다루기 위해 하나의 함수로 정의해서 16보다 작은지 큰지 check하는 부분 및 다시 16개의 array로 나누는 부분 등이 비효율적인 부분이 많아
	 * 2개로 나누는 mergesort에 비해 overhead가 크다.
	 */



	private static int merge2(int[] arr) {
		arr = mergeSort2(arr, n);
		int retVal = 0;
		for (int i = 0; i < n; i++) {
			if (i % 4 == 0) {
				retVal += arr[i] % 7;
			}
		}
		return retVal;
	}

	private static int[] mergeSort2(int[] arr, int length) {
		if (length > 1) {
			int[][] arrEntry = new int[16][];
			int[] kArr = new int[16];
			boolean[] isExist = new boolean[16];
			int start = 0;
			for (int i = 0; i < 16; i++) {
				kArr[i] = ((length * (i + 1)) / 16) - ((length * (i)) / 16);
			}

			for (int i = 0; i < 16; i++) {
				if ((kArr[i]) != 0) {
					arrEntry[i] = new int[kArr[i]];
					isExist[i] = true;
					System.arraycopy(arr, start, arrEntry[i], 0, kArr[i]);
					start += kArr[i];
					arrEntry[i] = mergeSort2(arrEntry[i], kArr[i]);
				} else {
					isExist[i] = false;
				}
			}
			arr = doMerge2(arrEntry, isExist);
		}
		return arr;
	}

	private static int[] doMerge2(int[][] dividedArr, boolean[] existArr) {
		int retArrSize = 0;
		int[] idx = new int[16];
		for (int i = 0; i < 16; i++) {
			if (existArr[i]) {
				retArrSize += dividedArr[i].length;
				idx[i] = 0;
			}
		}
		int[] retArr = new int[retArrSize];
		int retArrIndex = 0;
		while (true) {
			int endCnt = 0;
			for (int i = 0; i < 16; i++) {
				if (existArr[i] && (idx[i] >= dividedArr[i].length)) {
					existArr[i] = false;
				}
				if (!existArr[i]) {
					endCnt++;
				}
			}
			if (endCnt == 16) {
				break;
			}
			boolean executedFlag = false;
			int minIndexFlag = 0;
			int min = 0;
			for (int i = 0; i < 16; i++) {
				if (existArr[i]) {
					if (executedFlag) {
						if (dividedArr[i][idx[i]] < min) {
							min = dividedArr[i][idx[i]];
							minIndexFlag = i;
						}
					} else {
						min = dividedArr[i][idx[i]];
						minIndexFlag = i;
						executedFlag = true;
					}
				}
			}
			retArr[retArrIndex++] = min;
			idx[minIndexFlag]++;
		}
		return retArr;
	}



	/*
	 * MergeSort3은 16개로 나눈 후 heap 성질을 이용하여 효율적으로 합치는 Merge Sort이다.
	 * T(n) = mergeSort1(arr, n), Q(n) = doMerge1(arr[], Exist[]){arrEntry의 총 원소의 합 = n} 일 때
	 * T(n) = 16*T(n/16) + Q(n) + theta(n)
	 * 으로 나타낼 수 있고, 이 때 theta(n)은 System.arraycopy를 16번 call하여 총 n개의 element를 array에 단순 copy 하기 때문에 theta(n)으로 나타낼 수 있으며, 
	 * 16번의 arrEntry 할당, isExsit 할당 및 start 값 계산 등은 상수 시간으로  theta(n)에 포함된다.
	 * Q(n)은 heap을 이용하기 때문에 best case와 worst case에서 percolate down을 call한 수 만큼의 차이가 나므로 worst case에 대해서 먼저 구하면 
	 * n-16회의 삽입 후 percolateDown 및 recursive하게 tree의 가장 아래까지 가기와 단순 삽입 16회이다.
	 * 이는 다시 말하면 tree의 높이 * (n-16) + 16이고 식으로 나타내면 Q(n) = (log16/log2)*(n-16) + 16
	 * best case는 이보다 percolatedown이 덜 일어나는 경우지만 항상 1보다는 크므로 best case와 worst case 모두 theta(n)이다.
	 * T(n) = 16*T(n/16) + theta(n)이다.
	 * f(n) = theta(n), h(n) = n으로 f(n)/h(n) = theta(1)이므로, 마스터 정리에 의해 T(n) = theta(nlogn)이다.
	 * 다만 이 경우 16보다 작은 경우도 일괄적으로 다루기 위해 하나의 함수로 정의해서 16보다 작은지 큰지 check하는 부분 및 다시 16개의 array로 나누는 부분 등
	 * 16개로 나누는 과정에서 비효율적인 부분이 많아 2개로 나누는 MergeSort에 비해 이점을 가지지는 않는다.
	 */

	private static int merge3(int[] arr) {
		arr = mergeSort3(arr, n);
		int retVal = 0;
		for (int i = 0; i < n; i++) {
			if (i % 4 == 0) {
				retVal += arr[i] % 7;
			}
		}
		return retVal;
	}

	private static int[] mergeSort3(int[] arr, int length) {
		if (length > 1) {
			int[][] arrEntry = new int[16][];
			int[] kArr = new int[16];
			boolean[] isExist = new boolean[16];
			int start = 0;
			for (int i = 0; i < 16; i++) {
				kArr[i] = ((length * (i + 1)) / 16) - ((length * (i)) / 16);
			}

			for (int i = 0; i < 16; i++) {
				if ((kArr[i]) != 0) {
					arrEntry[i] = new int[kArr[i]];
					isExist[i] = true;
					System.arraycopy(arr, start, arrEntry[i], 0, kArr[i]);
					start += kArr[i];
					arrEntry[i] = mergeSort3(arrEntry[i], kArr[i]);
				} else {
					isExist[i] = false;
				}
			}
			arr = doMerge3(arrEntry, isExist);
		}
		return arr;
	}

	private static int[] doMerge3(int[][] dividedArr, boolean[] existArr) {
		int retArrSize = 0;
		LinkedList[] heap = new LinkedList[16];
		int heapIndex = 0;
		for (int i = 0; i < 16; i++) {
			if (existArr[i]) {
				retArrSize += dividedArr[i].length;
				LinkedList entryLL = new LinkedList(dividedArr[i][0]);
				heap[heapIndex++] = entryLL;
				LinkedList curLL = entryLL;
				for (int j = 1; j < dividedArr[i].length; j++) {
					curLL.setNext(new LinkedList(dividedArr[i][j]));
					curLL = curLL.getNext();
				}
			}
		}
		heap = heapify(heap, heapIndex);
		int[] retArr = new int[retArrSize];
		int retArrIndex = 0;
		while (heapIndex > 0) {
			retArr[retArrIndex++] = heap[0].getVal();
			if (heap[0].getNext() != null) {
				heap[0] = heap[0].getNext();
				heap = percolateDown(heap, 1, heapIndex);
			} else {
				heap[0] = heap[--heapIndex];
				heap[heapIndex] = null;
				heap = percolateDown(heap, 1, heapIndex);
			}
		}
		return retArr;
	}

	private static LinkedList[] heapify(LinkedList[] heap, int heapSize) {
		for (int i = heapSize / 2; i > 0; i--) {
			heap = percolateDown(heap, i, heapSize);
		}
		return heap;
	}

	private static LinkedList[] percolateDown(LinkedList[] heap, int i, int n) {
		int lChild = 2 * i;
		int rChild = 2 * i + 1;
		if (lChild <= n) {
			if ((rChild <= n) && (heap[lChild - 1].getVal() > heap[rChild - 1].getVal())) {
				lChild = rChild;
			}
			if (heap[i - 1].getVal() > heap[lChild - 1].getVal()) {
				LinkedList tmpLL = heap[i - 1];
				heap[i - 1] = heap[lChild - 1];
				heap[lChild - 1] = tmpLL;
				heap = percolateDown(heap, lChild, n);
			}
		}
		return heap;
	}

}

class LinkedList {
	int value;
	LinkedList next;

	public LinkedList() {
		value = 0;
		next = null;
	}

	public LinkedList(int val) {
		value = val;
		next = null;
	}

	LinkedList getNext() {
		return this.next;
	}

	void setNext(LinkedList nextLL) {
		this.next = nextLL;
	}

	int getVal() {
		return this.value;
	}

	void setVal(int val) {
		this.value = val;
	}
}