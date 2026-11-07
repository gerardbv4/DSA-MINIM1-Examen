const API_URL = "http://localhost:8080/dsaApp/drons";

async function fetchDrons() {
    const container = document.getElementById('drons');

    try
    {
        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const data = await response.json();
        container.innerHTML = "";

        data.forEach(dron => {
            const card = document.createElement('div');
            card.classList.add('dron-card');

            card.innerHTML = `
                    <h3>${dron.name || 'Unknown Dron'}</h3>
                    <p>${dron.fabricante || 'Unknown Fabricante'} ${dron.modelo || 'Unknown Modelo'}</p>
                    <p>${dron.hours ?? 'Unknown Hours'}</p>
                    <small>Ref: ${dron.id}</small>
                `;
            container.appendChild(card);
        });

    } catch (error) {
        console.error("Error al cargar drons:", error);
        container.innerHTML = `<p style="color: red; text-align: center;">No se pudo conectar con la API en ${API_URL}</p>`;
    }
}

fetchDrons();