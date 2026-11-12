const button = document.getElementById("checkApiButton");
const statusText = document.getElementById("apiStatus");

button.addEventListener("click", async () => {
    try {
        const response = await fetch("/api/health");
        const data = await response.json();

        statusText.textContent = data.message;
    } catch (error) {
        statusText.textContent = "Unable to connect to the API.";
    }
});