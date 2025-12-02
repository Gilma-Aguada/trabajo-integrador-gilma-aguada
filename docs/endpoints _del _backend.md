
Entidad     Operación          Método   Endpoint                          Descripción
-----------------------------------------------------------------------------------------------
Cliente     Crear              POST     /api/clientes                     Crea un nuevo cliente
Cliente     Leer todos         GET      /api/clientes                     Lista todos los clientes
Cliente     Leer por ID        GET      /api/clientes/{id}                Obtiene un cliente específico
Cliente     Actualizar         PUT      /api/clientes/{id}                Actualiza datos de un cliente
Cliente     Eliminar           DELETE   /api/clientes/{id}                Elimina un cliente
Cliente     Buscar por nombre  GET      /api/clientes/buscar?nombre=...   Busca clientes por nombre
Cliente     Buscar por email   GET      /api/clientes/email/{email}       Busca cliente por email

Producto    Crear              POST     /api/productos                    Crea un nuevo producto
Producto    Leer todos         GET      /api/productos                    Lista todos los productos
Producto    Leer por ID        GET      /api/productos/{id}               Obtiene un producto específico
Producto    Actualizar         PUT      /api/productos/{id}               Actualiza datos de un producto
Producto    Eliminar           DELETE   /api/productos/{id}               Elimina un producto
Producto    Buscar por nombre  GET      /api/productos/buscar?nombre=...  Busca productos por nombre
Producto    Con stock          GET      /api/productos/con-stock          Lista productos disponibles
Producto    Bajo stock         GET      /api/productos/bajo-stock         Lista productos con stock bajo

Pedido      Crear              POST     /api/pedidos                      Crea un nuevo pedido
Pedido      Leer todos         GET      /api/pedidos                      Lista todos los pedidos
Pedido      Leer por ID        GET      /api/pedidos/{id}                 Obtiene un pedido específico
Pedido      Actualizar         PUT      /api/pedidos/{id}                 Actualiza datos de un pedido
Pedido      Eliminar           DELETE   /api/pedidos/{id}                 Elimina un pedido
Pedido      Agregar detalle    POST     /api/pedidos/{id}/detalles        Agrega un producto al pedido
Pedido      Actualizar estado  PATCH    /api/pedidos/{id}/estado          Cambia estado de pago
Pedido      Por cliente        GET      /api/pedidos/cliente/{clienteId}  Lista pedidos de un cliente
Pedido      Pendientes         GET      /api/pedidos/pendientes           Lista pedidos pendientes
Pedido      Calcular total     GET      /api/pedidos/{id}/total           Devuelve el total del pedido