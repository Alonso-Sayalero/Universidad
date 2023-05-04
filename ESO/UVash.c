// Práctica realizada por Alonso Sayalero Blázquez
// Includes
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<sys/wait.h>
#include<stdbool.h>
#include<fcntl.h>

// Prototipos de los métodos
void imprimirError();
void consolaFichero(FILE* fichero);
void consolaBash();
void comandos(char** argumentos);
void ejecutarHilo(char** argumentos, int ficheroOpcional);
void separarArgumentos(char* buffer, char** argumentos);
int longitudArray(char** argumentos, int longitud);
int contarRedireccion(char** argumentos, int contador);
void procesarRedireccion(char** argumentos);

// Variable global
int tar = -2;

/*
Función main.

Comprueba el número de argumentos y dependiendo de estos activa el modo de la terminal correspondiente.
- 0 argumentos: Modo bash.
- 1 argumento: Modo ficheros.
- 2 o más argumentos: Error.
*/
int main(int argc, char* argv[])
{
	if(argc > 2)
	{
		imprimirError();
		exit(1);
	}
	switch(argc)
	{
		case 1:
			consolaBash();
			break;
		case 2:
			FILE* fich = fopen(argv[1], "r");
			if(fich == NULL)
			{
				imprimirError();
				exit(1);
			}else
			{
				consolaFichero(fich);
				fclose(fich);
			}
			break;
	}
	return 0;
}

// Imprime el error que salta en todos los casos de error.
void imprimirError()
{
	char error_message[30] = "An error has occurred\n";
	fprintf(stderr, "%s", error_message);
}

/*
Procesa el fichero introducido en el modo de ficheros.

- FILE* fichero: fichero a procesar.
*/
void consolaFichero(FILE* fichero)
{
	char* buff = (char*)malloc(1000*sizeof(char));
	char* argumentos[10];
	while(fgets(buff, 1000, fichero) != NULL)
    {
        buff = strsep(&buff, "\n");
		separarArgumentos(buff, argumentos);
		comandos(argumentos);
    }
}

/*
Simula la consola en modo bash.
*/
void consolaBash()
{
	char* buffer;
	char* argumentos[10];
	size_t tam = 1000;
	while(true)
	{
		printf("UVash> ");
		getline(&buffer, &tam, stdin);
		buffer = strsep(&buffer, "\n");
		separarArgumentos(buffer, argumentos);
		comandos(argumentos);
	}
}

/*
Procesa los comandos introducidos como array terminado en nulo.

- char** argumentos: array que contiene los comandos y termina en nulo.
*/
void comandos(char** argumentos)
{
	int l = 0;
	l = longitudArray(argumentos, l);
	if(strcmp(argumentos[0], "exit") == 0)
	{
		if(l == 1)
			exit(0);
		else
			imprimirError();
	}else if(strcmp(argumentos[0], "cd") == 0)
	{	
		if(l <= 0 || l > 2)
		{
			imprimirError();
		}
		else
		{
			if(chdir(argumentos[1]) == -1)
			{
				imprimirError();
			}
		}
	}else if(strcmp(argumentos[0], "InicioTar") == 0)//Inicio del examen
	{
		tar = open("salida.out", O_WRONLY | O_CREAT | O_APPEND);
		if(tar == -1)
		{
			imprimirError();
		}
	}else if(strcmp(argumentos[0], "FinTar") == 0)
	{
		close(tar);
		tar = -2;
		argumentos[0] = "tar";
		argumentos[1] = "-cf";
		argumentos[2] = "salida.tar";
		argumentos[3] = "salida.out";
		argumentos[4] = NULL;
		ejecutarHilo(argumentos, -2);
	}else // Fin examen
	{
		l = 0;
		l = contarRedireccion(argumentos, l);
		if(l > 1)
			imprimirError();
		else if(l == 0)
			ejecutarHilo(argumentos, -2);
		else
			procesarRedireccion(argumentos);
	}
}

/*
Lanza un hilo para ejecutar los comandos pasados por parámetro.

- char** argumentos: array con los parametros terminado en nulo.
- int ficheroOpcional: puntero de fichero en caso de haber redireccion.
	-2 en caso de no ser necesario.
	entero positivo en caso de ser necesario.
*/
void ejecutarHilo(char** argumentos, int ficheroOpcional)
{
	int pid = fork();
	if(pid == -1)
	{
		imprimirError();
	}else if(pid == 0)
	{
		if(ficheroOpcional != -2)
		{
			dup2(ficheroOpcional, 1);
			dup2(ficheroOpcional, 2);
		}else if(tar != -2) //Inicio examen
		{
			dup2(tar, 1);
			dup2(tar, 2);
		} //Fin examen
		if(execvp(argumentos[0], argumentos) == -1)
			imprimirError();
	}else
	{
		wait(NULL);
	}
}

/*
Crea el array del comando más sus argumentos terminado en nulo.

- char* buffer: buffer con los datos leidos de la entrada estándar.
- char** argumentos: array con el comando más sus argumentos terminado en nulo.
*/
void separarArgumentos(char* buffer, char** argumentos)
{
	for(int i = 0; i < 10; i++)
	{
		if(argumentos[i] != NULL) argumentos[i] = NULL;
	}
	int contador = 0;
	char* found;
	while( (found = strsep(&buffer," ")) != NULL )
	{
		if(contador < 10)
		{
			argumentos[contador] = found;
			contador++;
		}
	}
	argumentos[contador + 1] = NULL;
}

/*
Devuelve la longitud del array terminado en nulo.

- char** argumentos: array terminado en nulo.
- int longitud: longitud inicial, debe ser igual a 0.
*/
int longitudArray(char** argumentos, int longitud)
{
	while(argumentos[longitud] != NULL)
		longitud++;
	return longitud;
}

/*
Devuelve el número de veces que aparace el simbolo ">" en el array de comando.

- char** argumentos: array de comando terminado en nulo.
- int contador: numero de veces que aparece ">", debe ser igual a 0.
*/
int contarRedireccion(char** argumentos, int contador)
{
	int i = 0;
	while(argumentos[i] != NULL)
	{
		if(strcmp(argumentos[i], ">") == 0)
			contador++;
		i++;
	}
	return contador;
}

/*
Procesa la redirección de un comando a un fichero.

- char** argumentos: array de comando terminado en nulo.
*/
void procesarRedireccion(char** argumentos)
{
	char* comando[4];
	int contador = 0;
	int fichero;
	while(strcmp(argumentos[contador], ">") != 0)
	{
		comando[contador] = argumentos[contador];
		contador++;
	}
	if(contador == 0)
		imprimirError();
	comando[contador] = NULL;
	if(argumentos[contador + 1] == NULL || argumentos[contador + 2] != NULL)
	{
		imprimirError();
	}else
	{
		fichero = open(argumentos[contador + 1], O_WRONLY | O_CREAT);
		if(fichero == -1)
		{
			imprimirError();
			exit(1);
		}else
		{
			ejecutarHilo(comando, fichero);
			close(fichero);
		}
	}
}
