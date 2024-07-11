package com.alurachallenge.literatura.modelo;

public enum CategoriaIdioma {
    ESPANOL("[es]", "Español"),
    INGLES("[en]", "Ingles"),
    FRANCES("[fr]", "Frances"),
    PORTUGUES("[pt]", "Portugues");

    private String categoriaGutendex;
    private String categoriaEspanol;

    CategoriaIdioma(String categoriaGutendex, String categoriaEspanol){
        this.categoriaEspanol = categoriaEspanol;
        this.categoriaGutendex = categoriaGutendex;
    }
    public static CategoriaIdioma fromString(String text){
        for (CategoriaIdioma categoriaIdioma : CategoriaIdioma.values()){
            if (categoriaIdioma.categoriaGutendex.equalsIgnoreCase(text)){
                return categoriaIdioma;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

    public static CategoriaIdioma fromEspanol (String text){
        for (CategoriaIdioma categoriaIdioma : CategoriaIdioma.values()){
            if (categoriaIdioma.categoriaEspanol.equalsIgnoreCase(text)){
                return categoriaIdioma;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
