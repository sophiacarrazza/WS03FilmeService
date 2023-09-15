package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.FilmeDAO;
import model.Filme;
import spark.Request;
import spark.Response;

public class FilmeService {

    private FilmeDAO filmeDAO = new FilmeDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;
    private final int FORM_ORDERBY_NOME = 2;
    private final int FORM_ORDERBY_DURACAO = 3;

    public FilmeService() {
        makeForm();
    }

    public void makeForm() {
        makeForm(FORM_INSERT, new Filme(), FORM_ORDERBY_NOME);
    }

    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Filme(), orderBy);
    }

    public void makeForm(int tipo, Filme filme, int orderBy) {
        String nomeArquivo = "form.html";
        form = "";
        try {
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String umFilme = "";
        if (tipo != FORM_INSERT) {
            umFilme += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/filme/list/1\">Novo Filme</a></b></font></td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t</table>";
            umFilme += "\t<br>";
        }

        if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/filme/";
            String nome, genero, buttonLabel;
            if (tipo == FORM_INSERT) {
                action += "insert";
                nome = "Inserir Filme";
                genero = "Ação, Drama, ...";
                buttonLabel = "Inserir";
            } else {
                action += "update/" + filme.getId();
                nome = "Atualizar Filme (ID " + filme.getId() + ")";
                genero = filme.getGenero();
                buttonLabel = "Atualizar";
            }
            umFilme += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            umFilme += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + nome + "</b></font></td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\"" + nome + "\"></td>";
            umFilme += "\t\t\t<td>Duração: <input class=\"input--register\" type=\"text\" name=\"duracao\" value=\"" + filme.getDuracao() + "\"></td>";
            umFilme += "\t\t\t<td>Gênero: <input class=\"input--register\" type=\"text\" name=\"genero\" value=\"" + genero + "\"></td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\"" + filme.getDataFabricacao().toString() + "\"></td>";
            umFilme += "\t\t\t<td>Data de lançamento: <input class=\"input--register\" type=\"text\" name=\"dataLancamento\" value=\"" + filme.getDataLancamento().toString() + "\"></td>";
            umFilme += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\"" + buttonLabel + "\" class=\"input--main__style input--button\"></td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t</table>";
            umFilme += "\t</form>";
        } else if (tipo == FORM_DETAIL) {
            umFilme += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Filme (ID " + filme.getId() + ")</b></font></td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td>&nbsp;Nome: " + filme.getNome() + "</td>";
            umFilme += "\t\t\t<td>Duração: " + filme.getDuracao() + "</td>";
            umFilme += "\t\t\t<td>Gênero: " + filme.getGenero() + "</td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t\t<tr>";
            umFilme += "\t\t\t<td>&nbsp;Data de fabricação: " + filme.getDataFabricacao().toString() + "</td>";
            umFilme += "\t\t\t<td>Data de lançamento: " + filme.getDataLancamento().toString() + "</td>";
            umFilme += "\t\t\t<td>&nbsp;</td>";
            umFilme += "\t\t</tr>";
            umFilme += "\t</table>";
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }
        form = form.replaceFirst("<UM-FILME>", umFilme);

        String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
        list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Filmes</b></font></td></tr>\n" +
                "\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
                "\n<tr>\n" +
                "\t<td><a href=\"/filme/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
                "\t<td><a href=\"/filme/list/" + FORM_ORDERBY_NOME + "\"><b>Nome</b></a></td>\n" +
                "\t<td><a href=\"/filme/list/" + FORM_ORDERBY_DURACAO + "\"><b>Duração</b></a></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
                "</tr>\n";

        List<Filme> filmes;
        if (orderBy == FORM_ORDERBY_ID) {
            filmes = filmeDAO.getOrderByID();
        } else if (orderBy == FORM_ORDERBY_NOME) {
            filmes = filmeDAO.getOrderByNome();
        } else if (orderBy == FORM_ORDERBY_DURACAO) {
            filmes = filmeDAO.getOrderByDuracao();
        } else {
            filmes = filmeDAO.get();
        }

        int i = 0;
        String bgcolor = "";
        for (Filme f : filmes) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "\n<tr bgcolor=\"" + bgcolor + "\">\n" +
                    "\t<td>" + f.getId() + "</td>\n" +
                    "\t<td>" + f.getNome() + "</td>\n" +
                    "\t<td>" + f.getDuracao() + "</td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"/filme/" + f.getId() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"/filme/update/" + f.getId() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteFilme('" + f.getId() + "', '" + f.getNome() + "', '" + f.getDuracao() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "</tr>\n";
        }
        list += "</table>";
        form = form.replaceFirst("<LISTAR-FILME>", list);
    }

    public Object insert(Request request, Response response) {
        String nome = request.queryParams("nome");
        float duracao = Float.parseFloat(request.queryParams("duracao"));
        String genero = request.queryParams("genero");
        LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"));
        LocalDate dataLancamento = LocalDate.parse(request.queryParams("dataLancamento"));

        String resp = "";

        Filme filme = new Filme(-1, nome, duracao, genero, dataFabricacao, dataLancamento);

        if (filmeDAO.insert(filme)) {
            resp = "Filme (" + nome + ") inserido!";
            response.status(201); // 201 Created
        } else {
            resp = "Filme (" + nome + ") não inserido!";
            response.status(404); // 404 Not found
        }

        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Filme filme = filmeDAO.get(id);

        if (filme != null) {
            response.status(200); // success
            makeForm(FORM_DETAIL, filme, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Filme " + id + " não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }

        return form;
    }

    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Filme filme = filmeDAO.get(id);

        if (filme != null) {
            response.status(200); // success
            makeForm(FORM_UPDATE, filme, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Filme " + id + " não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }

        return form;
    }

    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Filme filme = filmeDAO.get(id);
        String resp = "";

        if (filme != null) {
            filme.setNome(request.queryParams("nome"));
            filme.setDuracao(Float.parseFloat(request.queryParams("duracao")));
            filme.setGenero(request.queryParams("genero"));
            filme.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
            filme.setDataLancamento(LocalDate.parse(request.queryParams("dataLancamento")));
            filmeDAO.update(filme);
            response.status(200); // success
            resp = "Filme (ID " + filme.getId() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Filme (ID " + filme.getId() + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Filme filme = filmeDAO.get(id);
        String resp = "";

        if (filme != null) {
            filmeDAO.delete(id);
            response.status(200); // success
            resp = "Filme (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Filme (" + id + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }
}
