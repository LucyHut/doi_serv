# doi_serv

This web app is a simple service that looks at an id parameter and returns
the mgi_logo.png image id the value of the parameter is contained in the ftp
report MGI_Elsevier.rpt.  Otherwise a transparent 1 pixel gif is returned.  

The report used for the id list is  configured in the init-param mapFile in 
the web.xml file. There are 2 special values for the id parameter; test and 
refresh. If the id parameter value submitted is test, the mgi_logo.png image
is returned.

If the id parameter value submitted is refresh, the servlet will 
refresh the id mappings from the mapFile.  The servlet address is doi/imageMap.
