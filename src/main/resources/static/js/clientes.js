/**
 * MÓDULO DE GESTIÓN DE CLIENTES
 * Consumo de API RESTful usando Fetch nativa
 * Maneja operaciones CRUD (Create, Read, Update, Delete) de clientes
 */

// URL base de la API REST para clientes
const API_URL = 'http://localhost:8080/api/clientes';
let modalInstance; // Instancia del modal de Bootstrap

/**
 * Evento de inicialización
 * Se ejecuta cuando el DOM está completamente cargado
 */
document.addEventListener('DOMContentLoaded', () => {
    cargarClientes();
    modalInstance = new bootstrap.Modal(document.getElementById('clienteModal'));
});

/**
 * Carga todos los clientes desde la API usando Fetch
 * @async
 * @returns {Promise<void>}
 */
async function cargarClientes() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Error al cargar clientes');
        
        const clientes = await response.json();
        mostrarClientes(clientes);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudieron cargar los clientes');
    }
}

/**
 * Renderiza la lista de clientes en la tabla
 * @param {Array} clientes - Array de clientes a mostrar
 */
function mostrarClientes(clientes) {
    const tbody = document.getElementById('tablaClientes');
    
    // Si no hay clientes, mostrar mensaje vacío
    if (clientes.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay clientes registrados</td></tr>';
        return;
    }
    
    // Renderizar cada cliente con botones de acción
    tbody.innerHTML = clientes.map(cliente => `
        <tr>
            <td>${cliente.idCliente}</td>
            <td>${cliente.nombre}</td>
            <td>${cliente.email}</td>
            <td>${cliente.telefono}</td>
            <td>
                <button class="btn btn-sm btn-warning" onclick="editarCliente(${cliente.idCliente})">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="eliminarCliente(${cliente.idCliente})">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

/**
 * Busca clientes por nombre usando Fetch
 * Si el campo está vacío, recarga todos los clientes
 * @async
 * @returns {Promise<void>}
 */
async function buscarClientes() {
    const nombre = document.getElementById('buscarNombre').value;
    
    // Si está vacío, mostrar todos
    if (!nombre.trim()) {
        cargarClientes();
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/buscar?nombre=${encodeURIComponent(nombre)}`);
        if (!response.ok) throw new Error('Error en la búsqueda');
        
        const clientes = await response.json();
        mostrarClientes(clientes);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al buscar clientes');
    }
}

/**
 * Abre el modal para crear un nuevo cliente
 * Limpia los campos del formulario
 */
function abrirModalNuevo() {
    document.getElementById('modalTitle').textContent = 'Nuevo Cliente';
    document.getElementById('formCliente').reset();
    document.getElementById('clienteId').value = '';
}

/**
 * Valida los datos de un cliente antes de guardarlo
 * @param {Object} cliente - Objeto con nombre, email y telefono
 * @returns {Array} Array de errores encontrados (vacío si es válido)
 */
function validarCliente(cliente) {
    const errores = [];
    
    // Validar nombre
    if (!cliente.nombre || cliente.nombre.trim() === '') {
        errores.push('El nombre es obligatorio');
    } else if (cliente.nombre.trim().length < 3) {
        errores.push('El nombre debe tener al menos 3 caracteres');
    } else if (cliente.nombre.trim().length > 50) {
        errores.push('El nombre no puede exceder 50 caracteres');
    }
    
    // Validar email
    if (!cliente.email || cliente.email.trim() === '') {
        errores.push('El email es obligatorio');
    } else {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(cliente.email)) {
            errores.push('El email no es válido');
        }
    }
    
    // Validar teléfono
    if (!cliente.telefono || cliente.telefono.trim() === '') {
        errores.push('El teléfono es obligatorio');
    } else {
        const telefonoLimpio = cliente.telefono.replace(/[- ]/g, '');
        if (!/^\d{7,15}$/.test(telefonoLimpio)) {
            errores.push('El teléfono debe contener entre 7 y 15 dígitos');
        }
    }
    
    return errores;
}

/**
 * Carga los datos de un cliente en el modal para editar
 * @async
 * @param {number} id - ID del cliente a editar
 * @returns {Promise<void>}
 */
async function editarCliente(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error('Error al cargar cliente');
        
        const cliente = await response.json();
        
        // Llenar el formulario con los datos del cliente
        document.getElementById('modalTitle').textContent = 'Editar Cliente';
        document.getElementById('clienteId').value = cliente.idCliente;
        document.getElementById('nombre').value = cliente.nombre;
        document.getElementById('email').value = cliente.email;
        document.getElementById('telefono').value = cliente.telefono;
        
        modalInstance.show();
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al cargar los datos del cliente');
    }
}

/**
 * Guarda un cliente nuevo o actualiza uno existente
 * Valida los datos antes de enviar a la API
 * @async
 * @returns {Promise<void>}
 */
async function guardarCliente() {
    const id = document.getElementById('clienteId').value;
    const cliente = {
        nombre: document.getElementById('nombre').value.trim(),
        email: document.getElementById('email').value.trim(),
        telefono: document.getElementById('telefono').value.trim()
    };
    
    // ✅ VALIDAR ANTES DE ENVIAR
    const errores = validarCliente(cliente);
    if (errores.length > 0) {
        const mensaje = errores.map((err, i) => `${i + 1}. ${err}`).join('\n');
        mostrarError('Validación fallida:\n\n' + mensaje);
        return; // Detener aquí
    }
    
    try {
        const url = id ? `${API_URL}/${id}` : API_URL;
        const method = id ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cliente)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Error al guardar');
        }
        
        modalInstance.hide();
        cargarClientes();
        mostrarExito(id ? 'Cliente actualizado correctamente' : 'Cliente creado correctamente');
    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message);
    }
}

/**
 * Elimina un cliente después de confirmar con el usuario
 * @async
 * @param {number} id - ID del cliente a eliminar
 * @returns {Promise<void>}
 */
async function eliminarCliente(id) {
    // Pedir confirmación al usuario
    if (!confirm('¿Está seguro de eliminar este cliente?')) return;
    
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Error al eliminar');
        
        cargarClientes();
        mostrarExito('Cliente eliminado correctamente');
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al eliminar el cliente');
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
