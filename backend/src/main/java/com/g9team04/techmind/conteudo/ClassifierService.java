package com.g9team04.techmind.conteudo;

import com.g9team04.techmind.conteudo.internal.ClassificacaoResponse;

public interface ClassifierService {
    ClassificacaoResponse classificar(String titulo, String texto);
}
