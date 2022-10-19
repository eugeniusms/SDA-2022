import java.util.Arrays;

public class SortingTest {

	public static void main(String[] args) {
		int[] arr = { 70, 23, 25, 33, 8, 9, 10, 45, 11, 60, 27 };

//		bubbleSort(arr);
//		selectionSort(arr);
//		insertionSort(arr);
//		shellSort(arr);
//		mergeSort(arr, 0, arr.length - 1);
		quickSort(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}

	static void bubbleSort(int[] arr) {
		for (int i = arr.length - 1; i >= 0; i--) {
			boolean swapped = false;
			for (int j = 0; j < i; j++) {
				if (arr[j] > arr[j + 1]) {
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
					swapped = true;
				}
			}

			if (!swapped)
				return;
		}
	}

	static void selectionSort(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			int minIdx = i;

			for (int j = i + 1; j < arr.length; j++)
				if (arr[j] < arr[minIdx])
					minIdx = j;

			int temp = arr[i];
			arr[i] = arr[minIdx];
			arr[minIdx] = temp;
		}
	}

	static void insertionSort(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			int temp = arr[i];
			int j = i;
			while (j > 0 && temp < arr[j - 1]) {
				arr[j] = arr[j - 1];
				j--;
			}

			arr[j] = temp;
		}
	}

	static void shellSort(int[] arr) {
		int n = arr.length;
		for (int gap = n / 2; gap > 0; gap /= 2) {
			for (int i = gap; i < n; i += gap) {
				int temp = arr[i];
				int j = i;
				while (j > 0 && temp < arr[j - gap]) {
					arr[j] = arr[j - gap];
					j -= gap;
				}

				arr[j] = temp;
			}
		}
	}

	static void mergeSort(int[] arr, int left, int right) {
		if (left < right) {
			int mid = (left + right) / 2;

			mergeSort(arr, left, mid);
			mergeSort(arr, mid + 1, right);

			merge(arr, left, mid, right);
		}
	}

	static void merge(int[] arr, int left, int mid, int right) {
		int n1 = mid - left + 1;
		int n2 = right - mid;

		int leftArr[] = new int[n1];
		int rightArr[] = new int[n2];

		for (int i = 0; i < n1; i++)
			leftArr[i] = arr[left + i];
		for (int j = 0; j < n2; j++)
			rightArr[j] = arr[mid + 1 + j];

		int i = 0, j = 0, k = left;

		while (i < n1 && j < n2)
			if (leftArr[i] <= rightArr[j])
				arr[k++] = leftArr[i++];
			else
				arr[k++] = rightArr[j++];

		while (i < n1)
			arr[k++] = leftArr[i++];

		while (j < n2)
			arr[k++] = rightArr[j++];
	}

	static void quickSort(int arr[], int left, int right) {
		if (left < right) {
			int pIdx = partition(arr, left, right);
			quickSort(arr, left, pIdx - 1);
			quickSort(arr, pIdx + 1, right);
		}
	}

	static int partition(int arr[], int left, int right) {
		int pivot = arr[right];
		int i = left, j = right - 1;

		while (i <= j) {
			while (i <= right && arr[i] < pivot)
				i++;

			while (j >= left && arr[j] >= pivot)
				j--;

			if (i < j) {
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
		int temp = arr[i];
		arr[i] = arr[right];
		arr[right] = temp;
		return i;
	}

}
