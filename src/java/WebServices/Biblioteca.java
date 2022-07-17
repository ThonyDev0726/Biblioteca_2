package WebServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService(serviceName = "Biblioteca")
public class Biblioteca {

    @WebMethod(operationName = "Biblioteca")
    public List Biblioteca(
            @WebParam(name = "estNombres") String estNombres,
            @WebParam(name = "estApellidos") String estApellidos,
            @WebParam(name = "estCedula") String estCedula,
            @WebParam(name = "estCarrera") String estCarrera,
            @WebParam(name = "estNivel") String estNivel,
            @WebParam(name = "estParalelo") String estParalelo,
            @WebParam(name = "estDireccion") String estDireccion,
            @WebParam(name = "estTelefono") String estTelefono,
            @WebParam(name = "estCorreo") String estCorreo) {

        ArrayList<String> estudianteLista = new ArrayList<>();
        estudianteLista.add(estNombres);
        estudianteLista.add(estApellidos);
        estudianteLista.add(estCedula);
        estudianteLista.add(estCarrera);
        estudianteLista.add(estNivel);
        estudianteLista.add(estParalelo);
        estudianteLista.add(estDireccion);
        estudianteLista.add(estTelefono);
        estudianteLista.add(estCorreo);
        return estudianteLista;
    }

    @WebMethod(operationName = "LIBRO_DATOS")
    public List LIBRO_DATOS(
            @WebParam(name = "libCodigo") String libCodigo,
            @WebParam(name = "libTitulo") String libTitulo,
            @WebParam(name = "libAutor") String libAutor,
            @WebParam(name = "libPag") Integer libPag,
            @WebParam(name = "libTipo") String libTipo,
            @WebParam(name = "libCarrera") String libCarrera,
            @WebParam(name = "libDescripcion") String libDescripcion) {
        ArrayList<String> LIBRO_DATOS_LISTA = new ArrayList<>();
        LIBRO_DATOS_LISTA.add(libCodigo);
        LIBRO_DATOS_LISTA.add(libTitulo);
        LIBRO_DATOS_LISTA.add(libAutor);
        LIBRO_DATOS_LISTA.add(libPag.toString());
        LIBRO_DATOS_LISTA.add(libTipo);
        LIBRO_DATOS_LISTA.add(libCarrera);
        LIBRO_DATOS_LISTA.add(libDescripcion);
        return LIBRO_DATOS_LISTA;
    }

    @WebMethod(operationName = "CALCULO_TOTAL")
    public List CALCULO_TOTAL(
            @WebParam(name = "DIA_ENTREGA") String DIA_ENTREGA,
            @WebParam(name = "MES_ENTREGA") String MES_ENTREGA,
            @WebParam(name = "ANO_ENTREGA") String ANO_ENTREGA,
            @WebParam(name = "HORA_ENTREGA") String HORA_ENTREGA,
            @WebParam(name = "MINUTOS_ENTREGA") String MINUTOS_ENTREGA,
            @WebParam(name = "ESTADO_ENTREGA") String ESTADO_ENTREGA)
            throws ParseException {
        /*OBJETOS PARA REALIZAR LOS REQUERIMIENTOS */
        Date FECHA = new Date();
        Calendar CALENDARIO = new GregorianCalendar();
        ZoneId ZONA_HORARIA = ZoneId.systemDefault();
        LocalDate DIA_ACTUAL = FECHA.toInstant().atZone(ZONA_HORARIA).toLocalDate();
        DateTimeFormatter DIA_ACTUAL_TIEMPO_FORMATEADOR = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        /* =========== VARIABLES DE RETORNO =========== */
        ArrayList<String> ARRAY_NO_HAY_ATENCION_HORARIO = new ArrayList<>();
        String att = "INTENTELO DE NUEVO A PARTIR DE LAS 14h:30min HASTA LAS 18h:30min";
        ARRAY_NO_HAY_ATENCION_HORARIO.add(att);
        ArrayList<String> ARRAY_FERIADO = new ArrayList<>();
        String attFeriados = "NO HAY ATENCION EN LOS FERIADOS";
        ARRAY_FERIADO.add(attFeriados);
        ArrayList<String> RESPUESTA_FINAL = new ArrayList<>();

        /* =========== VARIABLES PRESTAMO =========== */
        int MES_PRESTAMO = DIA_ACTUAL.getMonth().getValue();
        int DIA_PRESTAMO = DIA_ACTUAL.getDayOfMonth();
        String DIA_SEMANA_PRESTAMO = DIA_ACTUAL.getDayOfWeek().toString();
        String DIA_SEMANA_PRESTAMO_ESPANOL;
        int HORA_PRESTAMO = CALENDARIO.get(Calendar.HOUR_OF_DAY);
        String DIA_PERSTAMO_FORMATEADO = DIA_ACTUAL_TIEMPO_FORMATEADOR.format(LocalDateTime.now());
        /* =========== VARIABLES ENTREGA =========== */
        String FECHA_ENTREGA = DIA_ENTREGA + "-" + MES_ENTREGA + "-" + ANO_ENTREGA + " " + HORA_ENTREGA + ":" + MINUTOS_ENTREGA + ":00";
        String FECHA_ENTREGA_NORMAL = DIA_ENTREGA + "-" + MES_ENTREGA + "-" + ANO_ENTREGA;
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate FECHA_ENTREGA_FORMATEADA = LocalDate.parse(FECHA_ENTREGA_NORMAL, FORMATTER);
        String DIA_SEMANA_ENTREGA = FECHA_ENTREGA_FORMATEADA.getDayOfWeek().toString();

        if ((HORA_PRESTAMO >= 14)) {
            if (Integer.parseInt(HORA_ENTREGA) <= 18) {
                /* =========== VALIDACION DE LOS SABADOS Y DOMINGOS PARA NO LABORAR EN LOS PRESTAMOS =========== */
                if (DIA_SEMANA_PRESTAMO.equals("SATURDAY") || DIA_SEMANA_ENTREGA.equals("SATURDAY")) {
                    DIA_SEMANA_PRESTAMO_ESPANOL = "Sabados";
                    return ATTENCION_SABADOS_DOMINGOS(DIA_SEMANA_PRESTAMO_ESPANOL.toUpperCase());
                } else if (DIA_SEMANA_PRESTAMO.equals("SUNDAY") || DIA_SEMANA_ENTREGA.equals("SUNDAY")) {
                    DIA_SEMANA_PRESTAMO_ESPANOL = "Domingos";
                    return ATTENCION_SABADOS_DOMINGOS(DIA_SEMANA_PRESTAMO_ESPANOL.toUpperCase());
                }
                // =========== GESTION PARA LOS FERIADOS PARA LOS PRESTAMOS Y ENTREGAS =========== */
                if ((MES_PRESTAMO == 1 && DIA_PRESTAMO == 1) || (Integer.parseInt(MES_ENTREGA) == 7 && Integer.parseInt(DIA_ENTREGA) == 9)) {
                    return ARRAY_FERIADO;
                } else if ((MES_PRESTAMO == 1 && DIA_PRESTAMO == 1) || (Integer.parseInt(MES_ENTREGA) == 7 && Integer.parseInt(DIA_ENTREGA) == 26)) {
                    return ARRAY_FERIADO;
                } else if ((MES_PRESTAMO == 5 && DIA_PRESTAMO == 26) || (Integer.parseInt(MES_ENTREGA) == 5 && Integer.parseInt(DIA_ENTREGA) == 24)) {
                    return ARRAY_FERIADO;
                }
                System.out.println("HORA ENTREGA VALIDA");
                RESPUESTA_FINAL.add("SOLICITUD: " + DIA_PERSTAMO_FORMATEADO);
                RESPUESTA_FINAL.add("ENTREGA: " + FECHA_ENTREGA);
                RESPUESTA_FINAL.add(VER_DIFERENCIA_DE_FECHAS(DIA_PERSTAMO_FORMATEADO, FECHA_ENTREGA).toString());
                return RESPUESTA_FINAL;
            } else {
                return ARRAY_NO_HAY_ATENCION_HORARIO;
            }
        } else {
            /* =========== VALIDACION DE LOS SABADOS Y DOMINGOS PARA NO LABORAR EN LOS PRESTAMOS =========== */
            return ARRAY_NO_HAY_ATENCION_HORARIO;
        }
    }

    public List ATTENCION_SABADOS_DOMINGOS(String dia) {
        ArrayList<String> ARRAY_NO_HAY_ATENCION_SABADOS_DOMINGOS = new ArrayList<>();
        ARRAY_NO_HAY_ATENCION_SABADOS_DOMINGOS.add("NO HAY ATENCION LOS DIAS " + dia);
        return ARRAY_NO_HAY_ATENCION_SABADOS_DOMINGOS;
    }

    public static List VER_DIFERENCIA_DE_FECHAS(String DIA_PRESTAMO, String DIA_ENTREGA) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date d1 = sdf.parse(DIA_PRESTAMO);
            Date d2 = sdf.parse(DIA_ENTREGA);
            ArrayList<String> ARRAY_CALCULO = new ArrayList<>();
            long difference_In_Time = d2.getTime() - d1.getTime();
            long DIFERENCIA_SEGUNDOS = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time) % 60;
            long DIFERENCIA_MINUTOS = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;
            long DIFERENCIA_HORAS = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24;
            long DIFERENCIA_DIAS = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;
            long DIFERENCIA_ANOS = TimeUnit.MILLISECONDS.toDays(difference_In_Time) / 365l;
            ARRAY_CALCULO.add("DIFERENCIA DE SEGUNDOS: " + DIFERENCIA_SEGUNDOS);
            ARRAY_CALCULO.add("DIFERENCIA DE  MINUTO: S" + DIFERENCIA_MINUTOS);
            ARRAY_CALCULO.add("DIFERENCIA DE HORAS: " + DIFERENCIA_HORAS);
            ARRAY_CALCULO.add("DIFERENCIA DE DIAS: " + DIFERENCIA_DIAS);
            ARRAY_CALCULO.add("DIFERENCIA DE AÑOS: " + DIFERENCIA_ANOS);
            return ARRAY_CALCULO;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
