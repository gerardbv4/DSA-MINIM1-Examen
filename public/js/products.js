const API_URL = "http://localhost:8080/dsaApp/products";

async function fetchProducts() {
    const container = document.getElementById('products');

    try {
        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const data = await response.json();
        container.innerHTML = "";

        data.forEach(product => {
            const card = document.createElement('div');
            card.classList.add('product-card');

            card.innerHTML = `
                    <h3>${product.name || 'Unknown Product'}</h3>
                    <p>${product.price || 'Unknown Price'}</p>
                    <p>${product.sales ?? 'Unknown Sales'}</p>
                    <small>Ref: ${product.id}</small>
                `;
            container.appendChild(card);
        });

    } catch (error) {
        console.error("Error al cargar products:", error);
        container.innerHTML = `<p style="color: red; text-align: center;">No se pudo conectar con la API en ${API_URL}</p>`;
    }
}

fetchProducts();