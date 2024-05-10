package File;

import java.io.*;

import javax.swing.JOptionPane;

public class FileHorse {
	 public static  void readfile() {
	        StringBuilder st = new StringBuilder();
	        BufferedReader br = null;
	        try {
	            File file = new File("history.txt");
	            if (!file.exists()) {
	                JOptionPane.showMessageDialog(null, "Không có thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
	                return; // Thoát khỏi phương thức nếu file không tồn tại
	            }
	            br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                st.append(line).append("\n");
	            }
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        } finally {
	            try {
	                if (br != null)
	                    br.close();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	        JOptionPane.showMessageDialog(null, st.toString(), "Lịch sử trò chơi", JOptionPane.INFORMATION_MESSAGE);
	    }
	 public static void saveFile(String time, int color) throws IOException {
	        String content = "\nLịch sử trò chơi\n";
	        String filename = "history.txt";
	        clearFile(filename); // Gọi hàm clearFile để xóa hết dữ liệu trong file
	        FileWriter fw = null;
	        BufferedWriter bw = null;
	        try {
	            File file = new File(filename);
	            if (!file.exists()) {
	                file.createNewFile(); // Tạo file mới nếu không tồn tại
	            }
	            clearFile(filename);
	            fw = new FileWriter(file, true);
	            bw = new BufferedWriter(fw);
	            bw.write(content);
	            bw.write("Thời gian: " + time);
	            bw.write("\nNgười chơi " + color + " chiến thắng.\n");
	            bw.write("-------------------------------------");
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        } finally {
	            try {
	                if (bw != null)
	                    bw.close();
	                if (fw != null)
	                    fw.close();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }

	    // Phương thức clearFile để xóa hết dữ liệu trong file
	    public static void clearFile(String fileName) {
	        try (PrintWriter writer = new PrintWriter(fileName)) {
	            // Ghi dữ liệu trống vào file
	            writer.print("");
	        } catch (FileNotFoundException e) {
	            throw new RuntimeException(e);
	        }
	    }
}
