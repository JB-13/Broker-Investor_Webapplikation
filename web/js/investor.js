document.addEventListener('DOMContentLoaded', function () {
    document.getElementById("loginBtn").addEventListener("click", login);
    document.getElementById("logoutBtn").addEventListener("click", logout);
    document.getElementById("registerBtn").addEventListener("click", registerInvestor);
    document.getElementById("deleteBtn").addEventListener("click", confirmDeleteInvestor);
    document.getElementById("createPortfolioBtn").addEventListener("click", createPortfolio);
    document.getElementById("portfolioBrokerId").addEventListener("change", fetchBrokerAssets);

    fetchBrokers(); // Initial Broker laden


});

// Globale Variablen
globalInvestorId = null;
globalUsername = null;
globalAccessToken = null;



function showMessage(elementId, message, type) {
    const messageElement = document.getElementById(elementId);
    messageElement.className = `message ${type}`;
    messageElement.innerText = message;
    messageElement.style.display = 'block';

    // Timeout, um die Nachricht nach 5 Sekunden auszublenden (5000 Millisekunden)
    setTimeout(() => {
        hideMessage(elementId);
    }, 5000);
}

function hideMessage(elementId) {
    const messageElement = document.getElementById(elementId);
    messageElement.style.display = 'none';
}


async function login() {
    let username = document.getElementById("loginUsername").value;
    let password = document.getElementById("loginPassword").value;

    let loginData = {
        "username": username,
        "password": password
    };

    try {
        const response = await fetch("http://localhost:8080/access/investor", {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData),
        });


        if (!response.ok) {
            const errorData = await response.json();
            showMessage('loginMessage', errorData.message, 'error');
            return;
        }

        const result = await response.json();


        // Setzte globale Variablen
        globalInvestorId = result.id;
        globalUsername = result.username;
        globalAccessToken = result.credential;

        clearAll();
        showLoginData();
        getPortfoliosByInvestorId();
        toggleSections();
        console.log(result);

    } catch (error) {
        console.error("Error:", error);
        showMessage('loginMessage', "Wrong Username or Password", 'error');
    }
}

async function logout() {
    try {
        const response = await fetch("http://localhost:8080/access", {
            method: 'delete',
            headers: {
                'Accept': 'application/json',
                'accessToken': globalAccessToken.accessToken,
            },
        });

        const result = await response.json();

        // Setzte globale Variablen
        globalInvestorId = null;
        globalUsername = null;
        globalAccessToken = null;

        clearAll();
        showLoginData();
        toggleSections();
        console.log(result);

    } catch (error) {
        console.error("Error:", error);
    }
}

function showLoginData() {
    if (globalInvestorId != null) {
        document.getElementById("outInvestorId").innerHTML = globalInvestorId;
    } else {
        document.getElementById("outInvestorId").innerHTML = "Not defined";
    }

    if (globalUsername != null) {
        document.getElementById("outUsername").innerHTML = globalUsername;
    } else {
        document.getElementById("outUsername").innerHTML = "Not defined";
    }

    if (globalAccessToken != null) {
        document.getElementById("outAccessToken").innerHTML = JSON.stringify(globalAccessToken);
    } else {
        document.getElementById("outAccessToken").innerHTML = "Not defined";
    }
}



async function registerInvestor() {
    let firstname = document.getElementById("regFirstname").value;
    let lastname = document.getElementById("regLastname").value;
    let username = document.getElementById("regUsername").value;
    let password = document.getElementById("regPassword").value;

    let investor = {
        "firstname": firstname,
        "lastname": lastname,
        "username": username,
        "password": password
    }

    try {
        const response = await fetch("http://localhost:8080/investor",
            {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(investor),
            });

        const result = await response.json();
        if( !response.ok )
        {
            const errorData = await response.json();
            showMessage('assetsMessage', errorData.message, 'error');
            throw new Error("Error: createInvestor " + result.status );
        }

        // Setzte globale Variablen
        globalInvestorId = result.id;
        globalUsername = result.username;
        globalAccessToken = result.credential;

        clearAll();
        showMessage('registerMessage', 'Successfully registered!', 'success');
        console.log(result);
    } catch (error) {
        console.error("Error:", error);
        showMessage('registerMessage', "Username already taken", 'error');
    }
}



async function confirmDeleteInvestor() {
    const confirmation = confirm("Are you sure you want to delete your account?");
    if (confirmation) {
        try {
            // Überprüfen, ob der Investor angemeldet ist
            if (!globalInvestorId || !globalAccessToken) {
                showMessage('accountMessage', 'Investor not logged in.', 'error');
                return;
            }



            // Wenn keine Portfolios vorhanden sind, Account löschen
            await deleteInvestor();
        } catch (error) {
            console.error("Error confirming deletion:", error);
            showMessage('accountMessage', error.message, 'error');
        }
    }
}






async function deleteInvestor()
{
    try {
        const response = await fetch(`http://localhost:8080/investor/` + globalInvestorId,
            {
                method: 'delete',
                headers: {
                    'Accept': 'application/json',
                    'accessToken': globalAccessToken.accessToken,
                },
            });

        if (response.ok) {
            // Account erfolgreich gelöscht
            showMessage('accountMessage', 'Account successfully deleted!', 'success');

            // Setze globale Variablen zurück
            globalInvestorId = null;
            globalUsername = null;
            globalAccessToken = null;

            clearAll();
            showLoginData();
            toggleSections();
        } else {
            // Fehler beim Löschen des Accounts
            const errorData = await response.json();
            showMessage('accountMessage', errorData.message, 'error');
        }
    } catch (error) {
        // Fehler beim Aufrufen der API
        console.error("Error deleting account:", error);
        showMessage('accountMessage', error.message, 'error');
    }
}












async function createPortfolio() {
    const brokerId = document.getElementById('portfolioBrokerId').value; // Get selected broker ID
    const selectedAssets = Array.from(document.querySelectorAll('input[name="selectedAssets"]:checked'))
        .map(checkbox => ({ id: parseInt(checkbox.value) })); // Ensure assetIds are integers

    const portfolioData = {
        broker: { id: parseInt(brokerId) }, // BrokerId soll int sein
        assets: selectedAssets // Assets anstatt assetIds verwenden
    };

    try {
        const response = await fetch(`http://localhost:8080/investor/${globalInvestorId}/portfolios`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalAccessToken.accessToken,
            },
            body: JSON.stringify(portfolioData),
        });

        if (response.ok) {
            const result = await response.json(); // JSON response parsen
            showMessage('portfolioMessage', 'Portfolio created successfully!', 'success');
            getPortfoliosByInvestorId(); // Portfolios aktualisieren
        } else {
            const errorData = await response.json(); // JSON response parsen
            showMessage('portfolioMessage', errorData.message || errorData.error, 'error');
        }
    } catch (error) {
        console.error('Error creating portfolio:', error);
        showMessage('portfolioMessage', 'Failed to create portfolio', 'error');
    }
}








async function deletePortfolio(portfolioId) {
    try {
        const response = await fetch(`http://localhost:8080/investor/${globalInvestorId}/portfolios/${portfolioId}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalAccessToken.accessToken,
            },
        });

        if (response.ok) {
            showMessage('portfolioMessage', 'Portfolio sold successfully!', 'success');
            getPortfoliosByInvestorId(); // Aktualisiere die angezeigten Portfolios nach dem Löschen
        } else {
            const errorData = await response.json();
            showMessage('portfolioMessage', errorData.message, 'error');
        }
    } catch (error) {
        console.error('Error selling portfolio:', error);
        showStatusError(error);
        showMessage('portfolioMessage', 'Failed to sell portfolio', 'error');
    }
}






async function getPortfoliosByInvestorId() {
    try {
        const response = await fetch(`http://localhost:8080/investor/${globalInvestorId}/portfolios`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalAccessToken.accessToken,
            },
        });

        if (response.ok) {
            const portfolios = await response.json();
            showOwnPortfolios(portfolios); // Zeige die Portfolios an
        } else {
            const errorData = await response.json();
            showMessage('portfolioMessage', errorData.message, 'error');
            throw new Error(errorData.message);
        }
    } catch (error) {
        console.error('Error fetching portfolios:', error);
        showStatusError(error);
        showMessage('portfolioMessage', 'Failed to fetch portfolios', 'error');
    }
}




function showOwnPortfolios(portfolios) {
    const portfolioList = document.getElementById("portfolioList");
    portfolioList.innerHTML = ''; // Liste leer machen bevor man neue items hinzfügt

    portfolios.forEach(portfolio => {
        const listItem = document.createElement('li');

        listItem.innerHTML = `
            <div>
                <strong>Broker:</strong> ${portfolio.broker.username}
            </div>
            <div>
                <strong>Assets:</strong>
                <ul class="asset-list">
                    ${portfolio.assets ? portfolio.assets.map(asset => `<li>${asset.name} (${asset.kind})</li>`).join('') : ''}
                </ul>
            </div>
            <button class="deletePortfolioBtn" data-portfolio-id="${portfolio.id}">Sell Portfolio</button>
        `;

        const deleteButton = listItem.querySelector('.deletePortfolioBtn');
        deleteButton.addEventListener('click', async function () {
            await deletePortfolio(portfolio.id);
        });

        portfolioList.appendChild(listItem);
    });
}



async function fetchBrokers() {
    try {
        const response = await fetch('http://localhost:8080/broker', {
            method: 'GET',
            headers: { 'Accept': 'application/json' },
        });

        if (!response.ok) {
            const errorData = await response.json();
            showMessage('error', errorData.message, 'error');
            return;
        }

        const brokers = await response.json();
        const selectBroker = document.getElementById('portfolioBrokerId');
        selectBroker.innerHTML = '';

        // Add a default option
        const defaultOption = document.createElement('option');
        defaultOption.disabled = true;
        defaultOption.selected = true;
        defaultOption.hidden = true;
        defaultOption.textContent = 'Select Broker';
        selectBroker.appendChild(defaultOption);

        // Add brokers to the dropdown
        brokers.forEach(broker => {
            const option = document.createElement('option');
            option.value = broker.id;
            option.textContent = `${broker.username}, Company: ${broker.company}`;
            selectBroker.appendChild(option);
        });
    } catch (error) {
        console.error("Error fetching brokers:", error);
        showMessage('error', "Failed to fetch brokers", 'error');
    }
}






async function fetchBrokerAssets() {
    const brokerId = document.getElementById('portfolioBrokerId').value;

    try {
        const response = await fetch(`http://localhost:8080/broker/${brokerId}/assets`, {
            method: 'GET',
            headers: { 'Accept': 'application/json' },
        });

        if (!response.ok) {
            const errorData = await response.json();
            showMessage('error', errorData.message, 'error');
            return;
        }

        const assets = await response.json();
        const assetList = document.getElementById('assetList');
        assetList.innerHTML = '';

        assets.forEach(asset => {
            const assetItem = document.createElement('div');
            assetItem.className = 'asset-item';

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.id = `asset_${asset.id}`;
            checkbox.value = asset.id;
            checkbox.name = 'selectedAssets';
            assetItem.appendChild(checkbox);

            const label = document.createElement('label');
            label.htmlFor = `asset_${asset.id}`;
            label.textContent = `${asset.name} (${asset.kind})`;
            assetItem.appendChild(label);

            assetList.appendChild(assetItem);
        });
    } catch (error) {
        console.error("Error fetching broker assets:", error);
        showMessage('error', "Failed to fetch broker assets", 'error');
    }
}






function clearAll() {
    const errorElement = document.getElementById("error");
    const outAlleInvestorElement = document.getElementById("outAlleInvestor");

    if (errorElement) errorElement.innerHTML = "";
    if (outAlleInvestorElement) outAlleInvestorElement.innerHTML = "";

    const loginUsername = document.getElementById("loginUsername");
    const loginPassword = document.getElementById("loginPassword");
    if (loginUsername) loginUsername.value = "";
    if (loginPassword) loginPassword.value = "";


    const regFirstname = document.getElementById("regFirstname");
    const regLastname = document.getElementById("regLastname");
    const regUsername = document.getElementById("regUsername");
    const regPassword = document.getElementById("regPassword");


    if (regFirstname) regFirstname.value = "";
    if (regLastname) regLastname.value = "";
    if (regUsername) regUsername.value = "";
    if (regPassword) regPassword.value = "";


    const updateFirstname = document.getElementById("updateFirstname");
    const updateLastname = document.getElementById("updateLastname");
    const updatePassword = document.getElementById("updatePassword");


    if (updateFirstname) updateFirstname.value = "";
    if (updateLastname) updateLastname.value = "";
    if (updatePassword) updatePassword.value = "";


}

function showStatusError(result) {
    document.getElementById("error").innerHTML = "Statuscode " + result.status + " : " + result.message;
}




function toggleSections() {
    const loginSection = document.getElementById("loginSection");
    const registerSection = document.getElementById("registerSection");
    const accountSection = document.getElementById("accountSection");
    const portfolioSection = document.getElementById("portfolioSection");
    const ownPortfoliosSection = document.getElementById("ownPortfoliosSection");

    if (globalInvestorId) {
        loginSection.classList.add("hidden");
        registerSection.classList.add("hidden");
        accountSection.classList.remove("hidden");
        portfolioSection.classList.remove("hidden");
        ownPortfoliosSection.classList.remove("hidden");
    } else {
        loginSection.classList.remove("hidden");
        registerSection.classList.remove("hidden");
        accountSection.classList.add("hidden");
        portfolioSection.classList.add("hidden");
        ownPortfoliosSection.classList.add("hidden")
    }
}