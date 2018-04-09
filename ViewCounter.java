import java.io.*;

public class ViewCounter {
	public class video{
		double length;
		int views;
		String name;
		String path;
		byte[] bytestream;
		FileOutputStream fos;
		
		public video(byte[] bytestream, String path) throws IOException{
			this.bytestream = bytestream;
			this.path = path;
			this.fos = convert(this.bytestream,this.path);
		}
		
		public double play(){
			double t = System.currentTimeMillis();
			//Insert code to play video
			return t;
		}
		public void pause(double playstarttime){
			double ct = System.currentTimeMillis();
			double diff = ct- playstarttime;
			viewincrease(diff);
			//Insert code to pause video
		}
		public void viewincrease(double time){
			if(time>0.7*this.length)
				this.views++;
		}
	}
	public static FileOutputStream convert(byte[] buf, String path) throws IOException {
	    ByteArrayInputStream bis = new ByteArrayInputStream(buf);
	    FileOutputStream fos = new FileOutputStream(path);
	    byte[] b = new byte[1024];

	    for (int readNum; (readNum = bis.read(b)) != -1;) {
	        fos.write(b, 0, readNum);
	    }
	    return fos;
	}
	public static byte[] converttobs(String path) throws IOException {

	    FileInputStream fis = new FileInputStream(path);
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    byte[] b = new byte[1024];

	    for (int readNum; (readNum = fis.read(b)) != -1;) {
	        bos.write(b, 0, readNum);
	    }

	    byte[] bytes = bos.toByteArray();

	    return bytes;
	}
}
