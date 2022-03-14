package BAX442DDR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BandN {
	public static void main(String[] args) throws IOException, InterruptedException {

		try {

			/*
			 * connects to the server and download its HTML page
			 */

			// Loading the first page with 40 items
			Document doc = Jsoup.connect("https://www.barnesandnoble.com/b/books/_/N-1fZ29Z8q8?Nrpp=40&page=1")
					.userAgent(
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36")
					.get();

			
			Elements lis = doc.select("li.pb-s.mt-m.bd-bottom-disabled-gray.record.list-view-data");

			// Declaring a list to store all book items url.
			List<String> ItemURL = new ArrayList<String>();

			// Loop for storing all 40 Book Urls into String list declared above.
			for (Element li : lis) {
				System.out.println("https://www.barnesandnoble.com" + li.select("a.pTopXImageLink").attr("href"));
				ItemURL.add("https://www.barnesandnoble.com" + li.select("a.pTopXImageLink").attr("href"));

			}

			// Printing the size of list
			System.out.println("\nSize of List: " + ItemURL.size());

			int j = 1;

			for (String num : ItemURL) {

				Document doc2 = Jsoup.connect(num).userAgent(
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36")
						.get();
				// Calling File writer Function
				saveString(new File("bn_top100_" + j + ".htm"), doc2.html(), false);
				j++;
				Thread.sleep(5_000);
			}

			for (int i = 1; i <= 40; i++) {

				// Reading the downloaded HTML files cointaing all books information.
				String html = loadString(new File("bn_top100_" + i + ".htm"));

				// Parsing html to Jsoup object
				Document doc1 = Jsoup.parse(html);

				// Scrapping Overview section of the book
				Elements lis2 = doc1.select("div.overview-cntnt");

				System.out.println("\n*****Book " + i + "******");

				// Fetching the text inside the div selected.
				String str = lis2.text();

				// Printing the first 100 characters of the Overview paragraph
				System.out.println(str.substring(0, 100));

				System.out.println("\n");

			}

		} catch (IOException ex) {

			System.out.println("Problem with the connection...");

		}

	}

	public static boolean saveString(File f, String s, boolean append) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(f, append);
				OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				BufferedWriter bw = new BufferedWriter(osw);) {
			bw.write(s);
			bw.flush();
			return true;
		}
	}

	public static String loadString(File f) throws IOException {
		byte[] encoded = Files.readAllBytes(f.toPath());
		return new String(encoded, StandardCharsets.UTF_8);
	}

}
