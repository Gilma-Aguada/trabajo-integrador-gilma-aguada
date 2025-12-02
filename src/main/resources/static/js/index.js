// Configuración de la API
const API_URL = 'http://localhost:8080/api';

// Cargar estadísticas al iniciar
document.addEventListener('DOMContentLoaded', () => {
    cargarEstadisticas();
});

// Función para cargar estadísticas usando fetch
async function cargarEstadisticas() {
    try {
        // Obtener total de clientes
        const clientesResponse = await fetch(`${API_URL}/clientes`);
        const clientes = await clientesResponse.json();
        document.getElementById('totalClientes').textContent = clientes.length;

        // Obtener total de productos
        const productosResponse = await fetch(`${API_URL}/productos`);
        const productos = await productosResponse.json();
        document.getElementById('totalProductos').textContent = productos.length;

        // Obtener total de pedidos
        const pedidosResponse = await fetch(`${API_URL}/pedidos`);
        const pedidos = await pedidosResponse.json();
        document.getElementById('totalPedidos').textContent = pedidos.length;

        // Obtener pedidos pendientes
        const pendientesResponse = await fetch(`${API_URL}/pedidos/pendientes`);
        const pendientes = await pendientesResponse.json();
        document.getElementById('pedidosPendientes').textContent = pendientes.length;

    } catch (error) {
        console.error('Error al cargar estadísticas:', error);
        mostrarError('No se pudieron cargar las estadísticas');
    }
}

function mostrarError(mensaje) {
    console.error(mensaje);
}
