package br.com.caelum.leilao.ws.test;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

import br.com.caelum.leilao.modelo.Usuario;

public class UsuarioWSTest {

	@Test
	public void deveRetornarListaDeUsuarios() {
		XmlPath path = given().header("Application", "application/xml").get("/usuarios?_format=xml").andReturn()
				.xmlPath();

		List<Usuario> usuarios = path.getList("list.usuario", Usuario.class);

		// Usuario usuario1 = path.getObject("list.usuario[0]", Usuario.class);
		// Usuario usuario2 = path.getObject("list.usuario[1]", Usuario.class);

		Usuario esperado1 = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		Usuario esperado2 = new Usuario(2L, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");

		assertEquals(esperado1, usuarios.get(0));
		assertEquals(esperado2, usuarios.get(1));

	}

	@Test
	public void deveRetornarOUsuarioPeloId() {

		JsonPath path = given().queryParam("usuario.id", 1)
				// .parameter("usuario.id", 1)
				.header("Accept", "application/json").get("/usuarios/show").andReturn().jsonPath();

		Usuario usuario = path.getObject("usuario", Usuario.class);
		Usuario esperado = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");

		String nome = path.getString("usuario.nome");
		String nomeEsperado = "Mauricio Aniche";

		assertEquals(esperado, usuario);
		assertEquals(nome, nomeEsperado);

	}

	@Test
	public void deveAdicionarUmUsuario() {
		Usuario joao = new Usuario("Joao da Silva", "joao@dasilva.com");

		//given().header("Accept", "application/xml").contentType("application/xml").body(joao);

		XmlPath retorno = 
				given()
					.header("Accept", "application/xml")
					.contentType("application/xml")
					.body(joao)
					
				.expect()
					.statusCode(200)
					
				.when()
					.post("/usuarios")
					
				.andReturn()
					.xmlPath();

		Usuario resposta = retorno.getObject("usuario", Usuario.class);

		assertEquals("Joao da Silva", resposta.getNome());
		assertEquals("joao@dasilva.com", resposta.getEmail());
		
				given()
					.contentType("application/xml")
					.body(resposta)
				.expect()
					.statusCode(200)
				.when()
					.delete("/usuarios/deleta")
					
				.andReturn()
					.asString();
	}

}
