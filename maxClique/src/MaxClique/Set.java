package MaxClique;

import java.math.BigInteger;
import java.util.LinkedList;

public class Set {
	private BigInteger setRep;
	
	public Set() {
		//construct an empty set
		setRep = BigInteger.ZERO;
	}
	
	public Set(String set){
		setRep = new BigInteger(set,2);
	}
	
	private Set(BigInteger set){
		setRep = set;
	}
	
	public boolean contains(int n){
		return setRep.testBit(n);
	}
	
	public void removeAll(Set B){
		//remove all element in B
		this.setRep = this.setRep.and(B.setRep.not());
	}

	public boolean add(int n){
		//add n
		this.setRep = setRep.setBit(n);
		return true;
	}
	
	public void remove(int n){
		//remove n
		this.setRep = setRep.clearBit(n);
	}

	public int size(){
		//return the size of this set
		return setRep.bitCount();
	}
	public boolean getConnectiviry(int target){
		return setRep.testBit(target);
	}
	
	public static Set Intersection(Set A, Set B){
		//return the Intersection of A and B
		Set intersection = new Set(A.setRep.and(B.setRep));
		return intersection;
	}
	
	public static Set Union(Set A, Set B){
		//return the Union of A and B
		Set Union = new Set(A.setRep.or(B.setRep));
		return Union;
	}
	public LinkedList<Integer> getAllElements(){
		//get all elements in the list, store in a linked list

		LinkedList<Integer> allElements = new LinkedList<>();
		for(int i=0;i<setRep.bitLength();i++){
			if(setRep.testBit(i)){
				allElements.add(i);
			}
		}

		return allElements;
	}

	public boolean equals(Set B){
		return this.setRep.equals(B.setRep);
	}
	
	public Set clone(){
		return new Set(setRep);
	}
	
	public Set addAll(Set A)
	{
		this.setRep = A.setRep.or(setRep);
		return new Set(setRep);
	}
	
	public String toString(){
		return setRep.toString(2);
	}
}
