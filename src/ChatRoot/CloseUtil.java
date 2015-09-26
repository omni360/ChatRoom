package ChatRoot;
/**
 * 关闭所有流
 */
import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
	public static void closeAll(Closeable ... io){
		for(Closeable tmp:io){
			if(tmp != null){
				
				try {
					tmp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

}
