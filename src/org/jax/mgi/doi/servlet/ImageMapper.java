package org.jax.mgi.doi.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageMapper
 */
public class ImageMapper extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String mapLoc;
	private Set<String> idSet = new HashSet<String>();
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageMapper() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String image = "images/transparent.gif";
	
		
		if (id !=null && !"".equals(id)){
			if (idSet.contains(id) || "test".equals(id)) {
				image = "images/mgi_logo.png";
			} else if ("refresh".equals(id)){
				populateMap();
			}
		}
		
	    // Get the absolute path of the image
	    ServletContext sc = this.getServletContext();
	    String filename = sc.getRealPath(image);
	
	    // Get the MIME type of the image
	    String mimeType = sc.getMimeType(filename);
	    if (mimeType == null) {
	        sc.log("Could not get MIME type of " + filename);
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        return;
	    }
	
	    try {
		    // Set content type
	    	response.setContentType(mimeType);
		
		    // Set content size
		    File file = new File(filename);
		    response.setContentLength((int)file.length());
		
		    // Open the file and output streams
		    FileInputStream in = new FileInputStream(file);
		    OutputStream out = response.getOutputStream();
		
		    // Copy the contents of the file to the output stream
		    byte[] buf = new byte[1024];
		    int count = 0;
		    while ((count = in.read(buf)) >= 0) {
		        out.write(buf, 0, count);
		    }
		    in.close();
		    out.close();
	    } catch (Exception e){
	    	System.out.println(e.getMessage());
	    }		
		
		return;
	}
	
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	mapLoc = config.getInitParameter("mapFile");
    	populateMap();
		System.out.println("init()");
    }
    
    private void populateMap(){
		try {
			URL url = new URL(mapLoc);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				idSet.add(line.trim());
			}
			System.out.println("populateMap()");
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
