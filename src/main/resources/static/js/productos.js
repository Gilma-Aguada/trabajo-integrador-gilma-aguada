/**
 * MÓDULO DE GESTIÓN DE PRODUCTOS
 * Consumo de API RESTful usando Axios
 * Maneja operaciones CRUD (Create, Read, Update, Delete) de productos
 */

// URL base de la API REST para productos
const API_URL = 'http://localhost:8080/api/productos';
let modalInstance; // Instancia del modal de Bootstrap

/**
 * Evento de inicialización
 * Se ejecuta cuando el DOM está completamente cargado
 */
document.addEventListener('DOMContentLoaded', () => {
    cargarProductos();
    modalInstance = new bootstrap.Modal(document.getElementById('productoModal'));
});

/**
 * Carga todos los productos desde la API usando Axios
 * @async
 * @returns {Promise<void>}
 */
async function cargarProductos() {
    try {
        const response = await axios.get(API_URL);
        mostrarProductos(response.data);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudieron cargar los productos');
    }
}

/**
 * Renderiza la lista de productos en la tabla
 * @param {Array} productos - Array de productos a mostrar
 */
function mostrarProductos(productos) {
    const tbody = document.getElementById('tablaProductos');
    
    // Si no hay productos, mostrar mensaje vacío
    if (productos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay productos registrados</td></tr>';
        return;
    }
    
    // Renderizar cada producto con su estado de stock
    tbody.innerHTML = productos.map(producto => {
        const estadoStock = producto.stock < 10 ? 
            '<span class="badge bg-danger">Bajo Stock</span>' : 
            '<span class="badge bg-success">Disponible</span>';
        
        return `
            <tr>
                <td>${producto.idProducto}</td>
                <td>${producto.nombre}</td>
                <td>$${producto.precio.toFixed(2)}</td>
                <td>${producto.stock}</td>
                <td>${estadoStock}</td>
                <td>
                    <button class="btn btn-sm btn-warning" onclick="editarProducto(${producto.idProducto})">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarProducto(${producto.idProducto})">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

/**
 * Busca productos por nombre usando Axios
 * Si el campo está vacío, recarga todos los productos
 * @async
 * @returns {Promise<void>}
 */
async function buscarProductos() {
    const nombre = document.getElementById('buscarNombre').value;
    
    // Si está vacío, mostrar todos
    if (!nombre.trim()) {
        cargarProductos();
        return;
    }
    
    try {
        const response = await axios.get(`${API_URL}/buscar`, {
            params: { nombre: nombre }
        });
        mostrarProductos(response.data);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al buscar productos');
    }
}

/**
 * Carga solo los productos que tienen stock disponible
 * @async
 * @returns {Promise<void>}
 */
async function cargarConStock() {
    try {
        const response = await axios.get(`${API_URL}/con-stock`);
        mostrarProductos(response.data);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al cargar productos con stock');
    }
}

/**
 * Carga solo los productos con bajo stock (< 10 unidades)
 * @async
 * @returns {Promise<void>}
 */
async function cargarBajoStock() {
    try {
        const response = await axios.get(`${API_URL}/bajo-stock`);
        mostrarProductos(response.data);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al cargar productos con bajo stock');
    }
}

/**
 * Abre el modal para crear un nuevo producto
 * Limpia los campos del formulario
 */
function abrirModalNuevo() {
    document.getElementById('modalTitle').textContent = 'Nuevo Producto';
    document.getElementById('formProducto').reset();
    document.getElementById('productoId').value = '';
}

/**
 * Valida los datos de un producto antes de guardarlo
 * @param {Object} producto - Objeto con nombre, precio y stock
 * @returns {Array} Array de errores encontrados (vacío si es válido)
 */
function validarProducto(producto) {
    const errores = [];
    
    // Validar nombre
    if (!producto.nombre || producto.nombre.trim() === '') {
        errores.push('El nombre del producto es obligatorio');
    } else if (producto.nombre.trim().length < 3) {
        errores.push('El nombre debe tener al menos 3 caracteres');
    } else if (producto.nombre.trim().length > 100) {
        errores.push('El nombre no puede exceder 100 caracteres');
    }
    
    // Validar precio
    const precio = parseFloat(producto.precio);
    if (isNaN(precio) || precio <= 0) {
        errores.push('El precio debe ser un número positivo');
    } else if (precio > 1000000) {
        errores.push('El precio no puede ser mayor a 1.000.000');
    }
    
    // Validar stock
    const stock = parseInt(producto.stock);
    if (isNaN(stock) || stock < 0) {
        errores.push('El stock no puede ser negativo');
    } else if (stock > 999999) {
        errores.push('El stock no puede exceder 999.999 unidades');
    }
    
    return errores;
}

/**
 * Carga los datos de un producto en el modal para editar
 * @async
 * @param {number} id - ID del producto a editar
 * @returns {Promise<void>}
 */
async function editarProducto(id) {
    try {
        const response = await axios.get(`${API_URL}/${id}`);
        const producto = response.data;
        
        // Llenar el formulario con los datos del producto
        document.getElementById('modalTitle').textContent = 'Editar Producto';
        document.getElementById('productoId').value = producto.idProducto;
        document.getElementById('nombre').value = producto.nombre;
        document.getElementById('precio').value = producto.precio;
        document.getElementById('stock').value = producto.stock;
        
        modalInstance.show();
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al cargar los datos del producto');
    }
}

/**
 * Guarda un producto nuevo o actualiza uno existente
 * Valida los datos antes de enviar a la API
 * @async
 * @returns {Promise<void>}
 */
async function guardarProducto() {
    const id = document.getElementById('productoId').value;
    const producto = {
        nombre: document.getElementById('nombre').value.trim(),
        precio: parseFloat(document.getElementById('precio').value),
        stock: parseInt(document.getElementById('stock').value)
    };
    
    // ✅ VALIDAR ANTES DE ENVIAR
    const errores = validarProducto(producto);
    if (errores.length > 0) {
        const mensaje = errores.map((err, i) => `${i + 1}. ${err}`).join('\n');
        mostrarError('Validación fallida:\n\n' + mensaje);
        return; // Detener aquí
    }
    
    try {
        if (id) {
            // Actualizar producto existente
            await axios.put(`${API_URL}/${id}`, producto);
            mostrarExito('Producto actualizado correctamente');
        } else {
            // Crear nuevo producto
            await axios.post(API_URL, producto);
            mostrarExito('Producto creado correctamente');
        }
        
        modalInstance.hide();
        cargarProductos();
    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.response?.data?.message || 'Error al guardar el producto');
    }
}

/**
 * Elimina un producto después de confirmar con el usuario
 * @async
 * @param {number} id - ID del producto a eliminar
 * @returns {Promise<void>}
 */
async function eliminarProducto(id) {
    // Pedir confirmación al usuario
    if (!confirm('¿Está seguro de eliminar este producto?')) return;
    
    try {
        await axios.delete(`${API_URL}/${id}`);
        cargarProductos();
        mostrarExito('Producto eliminado correctamente');
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al eliminar el producto');
    }
}

/**
 * Muestra un mensaje de error al usuario
 * @param {string} mensaje - Texto del mensaje de error
 */
function mostrarError(mensaje) {
    alert('Error: ' + mensaje);
}

/**
 * Muestra un mensaje de éxito al usuario
 * @param {string} mensaje - Texto del mensaje de éxito
 */
function mostrarExito(mensaje) {
    alert(mensaje);
}
