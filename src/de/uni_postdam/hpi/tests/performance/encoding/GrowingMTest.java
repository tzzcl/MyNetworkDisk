package de.uni_postdam.hpi.tests.performance.encoding;

import static de.uni_postdam.hpi.utils.FileUtils.MB;

import java.io.File;

import org.junit.Test;

import de.uni_postdam.hpi.jerasure.Decoder;
import de.uni_postdam.hpi.jerasure.Encoder;
import de.uni_postdam.hpi.matrix.Schedule;
import de.uni_postdam.hpi.tests.performance.BasePerfTest;

public class GrowingMTest extends BasePerfTest {
	String fileName = "100mb";
	long fileSize = 100 * MB;
	
	int min_m = 1, max_m = 10;
	int k = 10, w = 8;
	
	@Test
	public void test() {

		File f = this.getFile(fileName);
		cleanAndCreateFile(f, fileSize);
		long t1, t2, size = f.length();
		
		for(int m = min_m; m <= max_m; m++) {
			String out = "";

			Schedule.copyCount = 0;
			Schedule.xorCount = 0;
			
			out += String.format("CR[%d:%d], w=%d:\t", k,m,w);
			Encoder enc = new Encoder(k, m, w);
			Decoder dec = new Decoder(f, k, m, w);
			
			t1 = System.currentTimeMillis();
			enc.encode(f);
			t2 = System.currentTimeMillis();
			
			out += String.format("Encoding: %d\t", t2 - t1);
			
			t1 = System.currentTimeMillis();
			dec.decode(size);
			t2 = System.currentTimeMillis();
			
			out += String.format("Decoding: %d", t2 - t1);
			
			out += String.format("\t xor: %d, copy: %d", Schedule.xorCount, Schedule.copyCount);
			
			System.out.println(out);
		}
	
	}
}
