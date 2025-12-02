
const API_URL = 'http://localhost:8080/api/pedidos';
const CLIENTES_URL = 'http://localhost:8080/api/clientes';
let modalInstance;
let clientesMap = {}; // Nuevo objeto para almacenar clientes mapeados por ID

// Cargar pedidos al iniciar
document.addEventListener('DOMContentLoaded', () => {
    // Es crucial cargar los clientes antes de los pedidos para el mapeo
    cargarClientesSelect() 
        .then(cargarPedidos)
        .catch(error => console.error("Fallo la inicialización:", error));
    
    modalInstance = new bootstrap.Modal(document.getElementById('pedidoModal'));
});

// Función para cargar clientes en el select Y crear el mapa de clientes
async function cargarClientesSelect() {
    try {
        const response = await fetch(CLIENTES_URL);
        const clientes = await response.json();
        
        const select = document.getElementById('cliente');
        select.innerHTML = '<option value="">Seleccione un cliente...</option>';
        
        // 1. Llenar el select
        clientes.forEach(c => {
            select.innerHTML += `<option value="${c.idCliente}">${c.nombre}</option>`;
            // 2. Crear el mapa de clientes para acceder rápidamente al nombre
            clientesMap[c.idCliente] = c.nombre; 
        });
        
    } catch (error) {
        console.error('Error cargando clientes:', error);
    }
}

// Función para cargar todos los pedidos
async function cargarPedidos() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Error al cargar pedidos');
        
        const pedidos = await response.json();
        mostrarPedidos(pedidos);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudieron cargar los pedidos');
    }
}

// Función para mostrar pedidos en la tabla
function mostrarPedidos(pedidos) {
    const tbody = document.getElementById('tablaPedidos');
    
    if (!tbody) { // Seguridad en caso de que el ID no exista
        console.error("No se encontró el elemento con ID 'tablaPedidos'.");
        return;
    }

    if (pedidos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay pedidos registrados</td></tr>';
        return;
    }
    
    tbody.innerHTML = pedidos.map(pedido => {
        const fecha = new Date(pedido.fecha).toLocaleString('es-AR');
        const badgeClass = {
            'PENDIENTE': 'bg-warning',
            'PAGADO': 'bg-success',
            'CANCELADO': 'bg-danger'
        }[pedido.estadoPago] || 'bg-secondary';
        
        // **CORRECCIÓN CLAVE:** Usamos clientesMap para obtener el nombre del cliente
        // El pedido solo tiene el ID, así que lo buscamos en el mapa.
        const nombreCliente = clientesMap[pedido.clienteId] || 'Cliente Desconocido'; 
        
        return `
            <tr>
                <td>${pedido.idPedido}</td>
                <td>${nombreCliente}</td> 
                <td>${fecha}</td>
                <td><span class="badge ${badgeClass}">${pedido.estadoPago}</span></td>
                <td>${pedido.direccionEnvio}</td>
                <td>
                    <button class="btn btn-sm btn-info text-white" onclick="verDetalle(${pedido.idPedido})">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-warning" onclick="editarPedido(${pedido.idPedido})">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarPedido(${pedido.idPedido})">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

// Cargar pedidos pendientes (Usa la función mostrarPedidos, no requiere cambios)
async function cargarPendientes() {
    try {
        const response = await fetch(`${API_URL}/pendientes`);
        const pedidos = await response.json();
        mostrarPedidos(pedidos);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al cargar pedidos pendientes');
    }
}

// Filtrar por estado (Usa la función mostrarPedidos, no requiere cambios)
async function filtrarPorEstado(estado) {
    try {
        const response = await fetch(API_URL);
        const todosLosPedidos = await response.json();
        const pedidosFiltrados = todosLosPedidos.filter(p => p.estadoPago === estado);
        mostrarPedidos(pedidosFiltrados);
    } catch (error) {
        console.error('Error:', error);
    }
}

// Abrir modal para nuevo pedido (No requiere cambios)
function abrirModalNuevo() {
    document.getElementById('modalTitle').textContent = 'Nuevo Pedido';
    document.getElementById('formPedido').reset();
    document.getElementById('pedidoId').value = '';
}

// Función para editar pedido
async function editarPedido(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        const pedido = await response.json();
        
        document.getElementById('modalTitle').textContent = 'Editar Pedido';
        document.getElementById('pedidoId').value = pedido.idPedido;
        
        // **CORRECCIÓN ADICIONAL:** Necesitamos el idCliente directo del pedido
        // Pero como el backend ignora el objeto Cliente, necesitamos una alternativa.
        // Si el backend envía 'idCliente' como campo directo, usar 'pedido.idCliente'.
        // Aquí asumimos que el pedido trae un campo 'idCliente' directo. Si no es así,
        // esto fallará y el backend debe ser modificado para incluir el ID del cliente.
        // Por ahora, lo dejamos como estaba, asumiendo que el campo 'cliente' ahora es 'clienteId'
        // o que la API de edición espera 'idCliente'.
        document.getElementById('cliente').value = pedido.clienteId || pedido.id_cliente; // Revisa el nombre del campo ID
        document.getElementById('estadoPago').value = pedido.estadoPago;
        document.getElementById('direccionEnvio').value = pedido.direccionEnvio;
        
        modalInstance.show();
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al cargar los datos del pedido');
    }
}

// Función para guardar pedido (No requiere cambios)
async function guardarPedido() {
    const id = document.getElementById('pedidoId').value;
    const pedido = {
        cliente: {
            idCliente: document.getElementById('cliente').value
        },
        estadoPago: document.getElementById('estadoPago').value,
        direccionEnvio: document.getElementById('direccionEnvio').value,
        fecha: new Date().toISOString()
    };
    
    try {
        const url = id ? `${API_URL}/${id}` : API_URL;
        const method = id ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(pedido)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Error al guardar');
        }
        
        modalInstance.hide();
        cargarPedidos();
        mostrarExito(id ? 'Pedido actualizado correctamente' : 'Pedido creado correctamente');
    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message);
    }
}

// Función para eliminar pedido (No requiere cambios)
async function eliminarPedido(id) {
    if (!confirm('¿Está seguro de eliminar este pedido?')) return;
    
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Error al eliminar');
        
        cargarPedidos();
        mostrarExito('Pedido eliminado correctamente');
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al eliminar el pedido');
    }
}

// Ver detalle del pedido
async function verDetalle(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        const pedido = await response.json();
        
        // **CORRECCIÓN CLAVE:** Usamos clientesMap para obtener el nombre del cliente
        const nombreCliente = clientesMap[pedido.clienteId] || 'Cliente Desconocido';
        
        alert(`Detalle del Pedido #${pedido.idPedido}\n\nCliente: ${nombreCliente}\nEstado: ${pedido.estadoPago}\nDirección: ${pedido.direccionEnvio}`);
    } catch (error) {
        console.error('Error:', error);
    }
}

// Funciones auxiliares (No requiere cambios)
function mostrarError(mensaje) {
    alert('Error: ' + mensaje);
}

function mostrarExito(mensaje) {
    alert(mensaje);
}
