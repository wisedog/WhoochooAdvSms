package net.wisedog.android.whooing.advsms.test;

import java.util.ArrayList;

import net.wisedog.android.whooing.advsms.CardInfo;
import junit.framework.TestCase;

public class CardInfoTest extends TestCase {
	
	public void testConvertIntArrayToString(){
		ArrayList<Integer> array = new ArrayList<Integer>();
		array.add(1);
		array.add(100);
		assertEquals("1,100", CardInfo.convertIntArrayToString(array));
		
		array.add(50);
		array.add(25);
		assertEquals("1,100,50,25", CardInfo.convertIntArrayToString(array));
	}
	
	public void testConvertStringToIntArray(){
		ArrayList<Integer> array = CardInfo.convertStringToIntArray("1,50,70");
		assertEquals(1, array.get(0).intValue());
		assertEquals(50, array.get(1).intValue());
		assertEquals(70, array.get(2).intValue());
	}
}
