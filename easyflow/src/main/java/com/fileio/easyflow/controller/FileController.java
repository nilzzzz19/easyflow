package com.fileio.easyflow.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "/Users/Nilay Nagar/Desktop/files";

	
	/*
	 * It creates a list of file names. It creates a File object that points to a
	 * directory specified by the UPLOADED_FOLDER constant. It retrieves an array of
	 * file names from the directory using the list() method of the File class. It
	 * adds each file name to the list of file names. It adds the list of file names
	 * to the Model object using the addAttribute() method, with the key "list". It
	 * returns the string "upload", which is the name of the Thymeleaf template that
	 * will be used to render the view.
	 */
	@GetMapping("/")
	public String index(Model model) {
		List<String> list = new ArrayList<String>();
		File files = new File(UPLOADED_FOLDER);
		String[] fileList = ((File) files).list();
		for (String name : fileList) {
			list.add(name);
			System.out.println(name);
		}
		model.addAttribute("list", list);
		return "upload";

		/*
		 * File folder = new File(UPLOADED_FOLDER); File[] listOfFiles =
		 * folder.listFiles(); List<String> fileNames = new ArrayList<>(); for (int i =
		 * 0; i < listOfFiles.length; i++) { if (listOfFiles[i].isFile()) {
		 * fileNames.add(listOfFiles[i].getName()); } } model.addAttribute("list",
		 * fileNames); return "index";
		 */
	}
	

	/*
	 * The method performs the following operations:
	 * 
	 * It checks if the uploaded file is empty. If it is, it adds a warning message
	 * to the Model object using the addAttribute method and returns the string
	 * "upload", which is the name of the Thymeleaf template that will be used to
	 * render the view.
	 * 
	 * If the file is not empty, it tries to perform the following operations:
	 * 
	 * It retrieves the content of the file as a byte array using the getBytes
	 * method of the MultipartFile object. It creates a Path object that points to
	 * the location where the file should be saved, using the UPLOADED_FOLDER
	 * constant and the original filename of the uploaded file. It writes the
	 * contents of the file to the specified location using the write method of the
	 * Files class. It adds a success message to the Model object indicating that
	 * the file was successfully uploaded. If an IOException occurs while trying to
	 * write the file to the disk, it adds an error message to the Model object
	 * using the addAttribute method and returns the string "upload", which is the
	 * name of the Thymeleaf template that will be used to render the view.
	 * 
	 * This method implements the logic for handling file uploads in the
	 * application. It saves the uploaded file to the specified directory and passes
	 * a message to the view indicating the result of the operation.
	 */
	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
		if (file.isEmpty()) {
			model.addAttribute("warning", "Please select a file to upload");
			return "upload";
		}
		
		try {

			byte[] bytes = file.getBytes(); // contents of a file
			Path path = Paths.get(UPLOADED_FOLDER 
					             + file.getOriginalFilename()); // path to a specific path as a dest
			Files.write(path, bytes); // so this action can get a direction.

			model.addAttribute("message", "You successfully uploaded '"
			                   + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			model.addAttribute("error", "Error");
			return "upload";
		}
		List<String> list = new ArrayList<String>();
		File files = new File(UPLOADED_FOLDER);
		String[] fileList = ((File) files).list();
		for (String name : fileList) {
			list.add(name);
		}
		model.addAttribute("list", list);
		return "upload";

	}
	

	/*
	 * It creates a File object using the UPLOADED_FOLDER constant and the file name
	 * specified in the URL.
	 * 
	 * It creates a Path object that points to the location of the file using the
	 * absolute path of the File object.
	 * 
	 * It reads the contents of the file as a byte array using the readAllBytes()
	 * method of the Files class and creates a ByteArrayResource object that wraps
	 * the byte array.
	 * 
	 * It returns a ResponseEntity object with the following properties:
	 * 
	 * The HTTP status code is set to 200 OK using the ok() method. The HTTP headers
	 * are set using the headers() method, which is a helper method that sets the
	 * content-disposition header for the file download. The content length is set
	 * using the contentLength() method and is the length of the file. The content
	 * type is set to application/octet-stream using the contentType() method and
	 * the MediaType class. The response body is set to the ByteArrayResource
	 * object.
	 */
	@GetMapping(path = "/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) 
			throws IOException {
		File file = new File(UPLOADED_FOLDER + name);
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		return ResponseEntity.ok()
				.headers(this.headers(name))
				.contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(resource);
	}


	/*
	 * It tries to delete the file using the deleteIfExists() method of the Files
	 * class, which takes a Path object as its argument. The Path object is created
	 * using the UPLOADED_FOLDER constant and the file name specified in the request
	 * parameter.
	 * 
	 * If the file is successfully deleted, the method returns a redirect:/
	 * response, which redirects the client to the root URL "/".
	 * 
	 * If an exception is thrown during the file deletion, the method returns a
	 * redirect:/ response, which also redirects the client to the root URL "/".
	 */
	@PostMapping(path = "/delete")
	public String delete(@RequestParam("name") String name)
			throws IOException {

		try {
			Files.deleteIfExists(Paths.get(UPLOADED_FOLDER + name));
		}

		catch (IOException e) {
			return "redirect:/";
		}
		return "redirect:/";
	}

	
	
	private HttpHeaders headers(String name) {
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
		header.add("Cache-Control", "no-cache, no-store," + " must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return header;

	}

}
