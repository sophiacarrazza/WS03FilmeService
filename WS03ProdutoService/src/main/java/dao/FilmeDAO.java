package dao;

import model.Filme;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO extends DAO {
    public FilmeDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean insert(Filme filme) {
        boolean status = false;
        try {
            String sql = "INSERT INTO filme (genero, nome, duracao, quantidade, datafabricacao, datalancamento) "
                    + "VALUES ('" + filme.getGenero() + "', "
                    + "'" + filme.getNome() + "', " + filme.getDuracao() + ", ?, ?);";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(filme.getDataFabricacao()));
            st.setDate(2, Date.valueOf(filme.getDataLancamento()));
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Filme get(int id) {
        Filme filme = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM filme WHERE id=" + id;
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                filme = new Filme(rs.getInt("id"), rs.getString("nome"), (float) rs.getDouble("duracao"),
                        rs.getString("genero"),
                        rs.getTimestamp("datafabricacao").toLocalDateTime(),
                        rs.getDate("datalancamento").toLocalDate());
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return filme;
    }

    public List<Filme> get() {
        return get("");
    }

    public List<Filme> getOrderByID() {
        return get("id");
    }

    public List<Filme> getOrderByNome() {
        return get("nome");
    }

    public List<Filme> getOrderByDuracao() {
        return get("duracao");
    }

    private List<Filme> get(String orderBy) {
        List<Filme> filmes = new ArrayList<Filme>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM filme" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Filme f = new Filme(rs.getInt("id"), rs.getString("nome"), (float) rs.getDouble("duracao"),
                        rs.getString("genero"),
                        rs.getTimestamp("datafabricacao").toLocalDateTime(),
                        rs.getDate("datalancamento").toLocalDate());
                filmes.add(f);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return filmes;
    }

    public boolean update(Filme filme) {
        boolean status = false;
        try {
            String sql = "UPDATE filme SET nome = '" + filme.getNome() + "', "
                    + "duracao = " + filme.getDuracao() + ", "
                    + "genero = '" + filme.getGenero() + "', "
                    + "datafabricacao = ?, "
                    + "datalancamento = ? WHERE id = " + filme.getId();
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(filme.getDataFabricacao()));
            st.setDate(2, Date.valueOf(filme.getDataLancamento()));
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM filme WHERE id = " + id);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    
}

