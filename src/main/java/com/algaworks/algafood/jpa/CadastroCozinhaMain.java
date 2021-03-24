package com.algaworks.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;

public class CadastroCozinhaMain {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		
		
		CozinhaDAO cozinhaDAO = applicationContext.getBean(CozinhaDAO.class);	
		
		Cozinha cozinha = new Cozinha();
		cozinha.setNome("Japonesa");
		
		cozinha = cozinhaDAO.adicionar(cozinha);
		
		System.out.printf("%d : %s \n", cozinha.getId(), cozinha.getNome());
	}
}

