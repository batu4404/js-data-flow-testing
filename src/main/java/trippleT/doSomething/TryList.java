package trippleT.doSomething;

import java.util.ArrayList;
import java.util.List;

public class TryList {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		List<Integer> sub = list.subList(0, list.size());
		List<Integer> list1 = new ArrayList(list);
		list.remove(list.size()-1);
		list.add(4);
		list.set(0, 111);
		List<Integer> sub2 = list.subList(0, list.size());
		List<Integer> list2 = new ArrayList(list);
		System.out.println(list1);
		System.out.println(list2);
	}
}
