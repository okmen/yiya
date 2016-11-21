import static org.junit.Assert.*;

import org.junit.Test;


public class tester {

	@Test
	public void test() {
		   try {
	            GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
	            generatorSqlmap.generator();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

}
