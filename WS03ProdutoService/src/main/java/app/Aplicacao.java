package app;

import static spark.Spark.*;
import service.FilmeService;

public class Aplicacao {
	
	private static FilmeService filmeService = new FilmeService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/filme/insert", (request, response) -> filmeService.insert(request, response));

        get("/filme/:id", (request, response) -> filmeService.get(request, response));
        
        get("/filme/list/:orderby", (request, response) -> filmeService.getAll(request, response));

        get("/filme/update/:id", (request, response) -> filmeService.getToUpdate(request, response));
        
        post("/filme/update/:id", (request, response) -> filmeService.update(request, response));
           
        get("/filme/delete/:id", (request, response) -> filmeService.delete(request, response));
    }
}
