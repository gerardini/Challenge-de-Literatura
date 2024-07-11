package com.alurachallenge.literatura.principal;


import com.alurachallenge.literatura.modelo.*;
import com.alurachallenge.literatura.repository.AutorRepository;
import com.alurachallenge.literatura.repository.LibroRepository;
import com.alurachallenge.literatura.service.ConsumoAPI;
import com.alurachallenge.literatura.service.ConvierteDatosJson;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatosJson convierteDatosJson = new ConvierteDatosJson();
    private List<Libro> libros;
    private List<Autor> autores;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestramenu(){

        Scanner teclado = new Scanner(System.in);


        //Variables para el menu

        String menu = """
                Bienvenido al sistema buscador de libros\n
                ****************************************************
                Elige la opcion deseada
                1.- Busca el libro por titulo.
                2.- Listar los libros registrados en el sistema.
                3.- Listar a los autores registrados en el sistema.
                4.- Listar a los autores vivos en un determinado tiempo.
                5.- Listar los libros por idioma.
                
                0.- Salir 
                *****************************************************
                """;

        //Inicio del programa
        int opcion = -1;

        while (opcion != 0 ){

            System.out.println(menu);
            System.out.println("Elija la operacion deseada");

            try {
                opcion = teclado.nextInt();
                teclado.nextLine();


                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresPorYear();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Terminando el programa...");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                        break;
                }
            }catch (InputMismatchException e){
                System.out.println("Ingrese un numero del menu");
                teclado.next();
                opcion = 0;
            }catch (Exception e){
                System.out.println("Opcion no valida");

            }



        }
       teclado.close();

    }



    private void listarLibrosPorIdioma() {
        String menuIdioma = """
                Ingrese el idioma para buscar los libros: 
                es >> Español
                en >> Ingles
                fr >> Frances 
                pt >> Portugues
                """;
        //System.out.println("Opcion 5");
        System.out.println(menuIdioma);
        String idiomaBuscado = teclado.nextLine();
        CategoriaIdioma idioma = null;
        //teclado.next();
        switch (idiomaBuscado){
            case "es":
                idioma = CategoriaIdioma.fromEspanol("Español") ;
                break;
            case "en":
                idioma = CategoriaIdioma.fromEspanol("Ingles") ;
                break;
            case "fr":
                idioma = CategoriaIdioma.fromEspanol("Frances") ;
                break;
            case "pt":
                idioma = CategoriaIdioma.fromEspanol("Portugues");
                break;
            default:
                System.out.println("Entrada inválida.");
                return;

        }
        buscarPorIdioma(idioma);

    }

    private void buscarPorIdioma(CategoriaIdioma idioma){
        libros = libroRepository.findLibrosByidioma(idioma);
        if (libros.isEmpty()){
            System.out.println("No hay libros registrados");
        } else {
            libros.stream().forEach(System.out::println);
        }
    }

    private void listarAutoresPorYear() {

        //System.out.println("Opcion 4");
        System.out.println("Ingrese el año vivo de Autore(s) que desea buscar: ");
        try {
            Integer year = teclado.nextInt();
            autores = autorRepository.findAutoresByYear(year);
            if (autores.isEmpty()){
                System.out.println("No hay autores en ese rango");
            } else {
                autores.stream().forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("Ingrese un año correcto");
            teclado.nextLine();
        }

    }


    private void listarAutoresRegistrados() {
        //System.out.println("Opcion 3");
        autores = autorRepository.findAll();
        autores.stream().forEach(System.out::println);
    }

    private void listarLibrosRegistrados() {
        //System.out.println("Opcion 2");
        //libros.forEach(System.out::println);
        libros = libroRepository.findAll();
        libros.stream().forEach(System.out::println);

    }

    private String realizarConsulta (){
        //System.out.println("Opcion 1");
        System.out.println("Escribe el nombre del libro a buscar: ");
        var nombreLibro = teclado.nextLine();
        //String url= "https://gutendex.com/books/?search=Quijote";
        String url = URL_BASE + "?search=" + nombreLibro.replace(" ", "%20");
        System.out.println("Esperando la respuesta...");
        String respuesta = consumoAPI.obtenerDatosApi(url);
        return respuesta;
    }

    private void buscarLibroPorTitulo() {
        String respuesta = realizarConsulta();
        DatosConsultaAPI datosConsultaAPI =convierteDatosJson.obtenerDatos(respuesta, DatosConsultaAPI.class);
        if (datosConsultaAPI.numeroLibros() !=0) {
            DatosLibro primerLibro = datosConsultaAPI.resultado().get(0);
            Autor autorLibro = new Autor(primerLibro.autores().get(0));
            Optional<Libro> libroBase = libroRepository.findLibroBytitulo(primerLibro.titulo());
            if (libroBase.isPresent()) {
                System.out.println("No se puede registrar el mismo líbro ");
                //System.out.println(libroBase);
            } else {
                Optional<Autor> autorDeBase = autorRepository.findBynombre(autorLibro.getNombre());
                if (autorDeBase.isPresent()) {
                    autorLibro = autorDeBase.get();
                } else {
                    autorRepository.save(autorLibro);
                }

                Libro libro = new Libro(primerLibro);
                libro.setAutor(autorLibro);
                libroRepository.save(libro);
                System.out.println(libro);
            }
        } else {
            System.out.println("Líbro no encontrado...");
        }
    }



}
