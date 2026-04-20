
// 1. Define the Interface (The Shape of a Pokemon)
interface PokemonData {
    name: string;
    id: number;
    sprites: { front_default: string };
    types: { type: { name: string } }[];
}

// 2. Select Elements
const container = document.getElementById('pokedex-container') as HTMLElement;
const input = document.getElementById('pokemonName') as HTMLInputElement;
const btn = document.getElementById('searchBtn') as HTMLButtonElement;

// 3. The Fetch Function
async function getPokemon(name: string): Promise<void> {
    try {
        container.innerHTML = "<p>Searching PokeBall...</p>";
        
        const response = await fetch(`https://pokeapi.co/api/v2/pokemon/${name.toLowerCase()}`);
        
        if (!response.ok) throw new Error("Pokemon not found!");

        const data: PokemonData = await response.json();
        renderCard(data);
        
    } catch (error) {
        container.innerHTML = `<p style="color: #ff1f1f;">Error: ${error}</p>`;
    }
}

// 4. The UI Function
function renderCard(pokemon: PokemonData): void {
    const type = pokemon.types[0].type.name;
    
    container.innerHTML = `
        <div class="pokemon-card">
            <small>#${pokemon.id.toString().padStart(3, '0')}</small>
            <img src="${pokemon.sprites.front_default}" alt="${pokemon.name}">
            <h2>${pokemon.name.toUpperCase()}</h2>
            <span class="type-badge">${type}</span>
        </div>
    `;
}

// 5. Event Listener
btn.addEventListener('click', () => {
    if (input.value) getPokemon(input.value);
});
