package br.com.caelum.leilao.ws.test;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

import br.com.caelum.leilao.modelo.Leilao;
import br.com.caelum.leilao.modelo.Usuario;

public class LeilaoWSTest {

	@Test
	public void deveRetornarLeilaoPeloId() {

		JsonPath path = given().parameter("leilao.id", 1).header("Accept", "application/json").get("leiloes/show")
				.andReturn().jsonPath();

		Leilao leilao = path.getObject("leilao", Leilao.class);

		Usuario mauricio = new Usuario(1l, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		Leilao esperado = new Leilao(1l, "Geladeira", 800.0, mauricio, false);

		Assert.assertEquals(esperado, leilao);

	}

	@Test
	public void deveRetornarTotaldeLeiloes() {

		XmlPath path = given().header("Accept", "application/xml").get("leiloes/total").andReturn().xmlPath();

		int total = path.getInt("int");

		int esperado = 2;

		Assert.assertEquals(esperado, total);

	}

	@Test
    public void deveInserirLeiloes() {
        Usuario mauricio = new Usuario(1l, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
        Leilao leiloes = new Leilao(1l, "Geladeira", 800.0, mauricio, false);

        XmlPath retorno = 
                given()
                    .header("Accept", ContentType.XML)
                    .contentType(ContentType.XML)
                    .body(leiloes)
                .expect()
                    .statusCode(200)
                .when()
                    .post("/leiloes")
                .andReturn()
                    .xmlPath();

        Leilao resposta = retorno.getObject("leilao", Leilao.class);

        assertEquals("Geladeira", resposta.getNome());

        // deletando aqui
        given()
            .contentType(ContentType.XML)
            .body(resposta)
        .expect()
            .statusCode(200)
        .when()
            .delete("/leiloes/deletar")
        .andReturn().asString();
    }

}
