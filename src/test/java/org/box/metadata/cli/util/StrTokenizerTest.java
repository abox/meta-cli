package org.box.metadata.cli.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class StrTokenizerTest {

	@Test
	public void test1() {

		Random r = new Random();
		for (int li = 0; li < 2000; li++) {
			StringBuilder sb = new StringBuilder();
			for (int ci = 0; ci < 20; ci++) {
				sb.append(r.nextInt(2) == 0 ? " " : "a");
			}
			String str = sb.toString();

			String token = null;
			StrTokenizer tokenizer = new StrTokenizer(str, ' ');
			List<String> res = new ArrayList<>();
			while ((token = tokenizer.nextToken()) != null)
				res.add(token);

		// 	nice view ^)
		//	System.out.println("compare: " + str);
			String[] exp = str.split("\\s+");
			String[] actual = res.toArray(new String[res.size()]);
			if (actual.length > exp.length) {
				Assert.fail("comparing: " + str + ";arr1: "
						+ Arrays.toString(exp) + ";arr2: "
						+ Arrays.toString(actual));
			}

			int j = 0;
			for (int i = 0; i < exp.length; i++) {
				if (exp[i].length() == 0)
					continue;

				if (!exp[i].equals(actual[j++])) {
					Assert.fail("comparing: " + str + ";arr1: "
							+ Arrays.toString(exp) + ";arr2: "
							+ Arrays.toString(actual));
				}
			}
		}
	}

	@Test
	public void test2() {
		{
			String str = "a \"a a\" a";

			String token = null;
			StrTokenizer tokenizer = new StrTokenizer(str, ' ');
			List<String> res = new ArrayList<>();
			while ((token = tokenizer.nextTokenUnquoted()) != null) {
				res.add(token);
			}

			String[] actuals = res.toArray(new String[res.size()]);

			Assert.assertArrayEquals(new String[] { "a", "a a", "a" }, actuals);
		}

		{
			String str = "a a\\ a a";

			String token = null;
			StrTokenizer tokenizer = new StrTokenizer(str, ' ');
			List<String> res = new ArrayList<>();
			while ((token = tokenizer.nextTokenUnquoted()) != null) {
				res.add(token);
			}

			String[] actuals = res.toArray(new String[res.size()]);

			Assert.assertArrayEquals(new String[] { "a", "a a", "a" }, actuals);
		}
	}

	@Test
	public void test3() {
		{
			String str = "a \"a a\\\" a";
	
			String token = null;
			StrTokenizer tokenizer = new StrTokenizer(str, ' ');
			List<String> res = new ArrayList<>();
			while ((token = tokenizer.nextTokenUnquoted()) != null) {
				res.add(token);
			}
	
			String[] actuals = res.toArray(new String[res.size()]);
	
			Assert.assertArrayEquals(new String[] { "a", "\"a a\" a" }, actuals);
			Assert.assertTrue(tokenizer.isStringIncomplete());
		}
		
		{
			String str = "a \"a a\\\\\" a";
	
			String token = null;
			StrTokenizer tokenizer = new StrTokenizer(str, ' ');
			List<String> res = new ArrayList<>();
			while ((token = tokenizer.nextTokenUnquoted()) != null) {
				res.add(token);
			}
	
			String[] actuals = res.toArray(new String[res.size()]);
	
			Assert.assertArrayEquals(new String[] { "a", "a a\\", "a" }, actuals);
			Assert.assertFalse(tokenizer.isStringIncomplete());
		}
		
		{
			String str = "a \"a a\\\\\\\" a";
	
			String token = null;
			StrTokenizer tokenizer = new StrTokenizer(str, ' ');
			List<String> res = new ArrayList<>();
			while ((token = tokenizer.nextTokenUnquoted()) != null) {
				res.add(token);
			}
	
			String[] actuals = res.toArray(new String[res.size()]);
	
			Assert.assertArrayEquals(new String[] { "a", "\"a a\\\" a" }, actuals);
			Assert.assertTrue(tokenizer.isStringIncomplete());
		}


	}
}
