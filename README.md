# 🔧 ms-operador – Microservicio Operador de Productos

Este microservicio forma parte de una arquitectura basada en microservicios para la gestión de un sistema de inventario distribuido. Su responsabilidad principal es ejecutar operaciones compuestas sobre productos gestionados por el microservicio `ms-buscador`, tales como el registro de pedidos y devoluciones.

## 🚀 Tecnologías y Dependencias

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Cloud OpenFeign
- Eureka Client
- PostgreSQL
- Maven

## 🧩 Rol dentro de la Arquitectura

`ms-operador` se comunica con el microservicio `ms-buscador` a través de **Eureka** y del **Cloud Gateway**, sin necesidad de conocer su dirección IP o puerto. Las llamadas entre microservicios se resuelven mediante descubrimiento de servicios, promoviendo el desacoplamiento.

Este microservicio tiene su **propia base de datos** y no mantiene relaciones físicas con la base de datos de `ms-buscador`.

## 📄 Funcionalidad Principal

- 📦 **Registro de Pedidos:** Agrupación de productos consultando a `ms-buscador` para verificar existencia y disponibilidad (stock).
- 🔍 **Consulta de Pedidos:** Consulta de pedidos por identificador.
- ♻️ **Registro de Devoluciones:** Asociadas a un pedido. La devolución actualiza el stock del producto a través del microservicio `ms-buscador`.

## 📡 Endpoints REST

### 🧾 Pedidos

- `POST /api/pedidos` – Registra un nuevo pedido. Valida la existencia y stock de cada producto mediante peticiones al microservicio `ms-buscador`.
- `GET /api/pedidos/{id}` – Consulta un pedido por su identificador.

### 🔁 Devoluciones

- `POST /api/devoluciones` – Registra una devolución a partir de un pedido previamente registrado. Actualiza el stock del producto correspondiente mediante `ms-buscador`.

## 🔗 Comunicación con ms-buscador

Las llamadas a `ms-buscador` se realizan mediante **OpenFeign** y resolviendo nombres de servicio registrados en **Eureka**, sin necesidad de especificar direcciones IP ni puertos.

Ejemplo de cliente Feign:

```java
@FeignClient(name = "ms-buscador")
public interface ProductoClient {

    @GetMapping("/api/productos/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable Long id);

    @PatchMapping("/api/productos/{id}")
    void actualizarStock(@PathVariable Long id, @RequestBody StockDTO stock);
}
````

## 📄 Autor

Desarrollado por: jagudo2514@gmail.com.
