document.addEventListener('DOMContentLoaded', function () {
    document.getElementById("loginBtn").addEventListener("click", login);
    document.getElementById("logoutBtn").addEventListener("click", logout);
    document.getElementById("registerBtn").addEventListener("click", registerBroker);
    document.getElementById("deleteBtn").addEventListener("click", confirmDeleteBroker);
    document.getElementById("createAssetForm").addEventListener("submit", function (event) {
        event.preventDefault(); // Formulare senden verhindern
        const assetName = document.getElementById("assetName").value;
        const assetKind = document.getElementById("assetKind").value;
        createAsset(assetName, assetKind);
    });


});

// Globale Variablen
globalBrokerId = null;
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
        const response = await fetch("http://localhost:8080/access/broker", {
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
        globalBrokerId = result.id;
        globalUsername = result.username;
        globalAccessToken = result.credential;

        clearAll();
        showLoginData();
        toggleSections();
        getAssetsByBrokerId();
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
        globalBrokerId = null;
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
    if (globalBrokerId != null) {
        document.getElementById("outBrokerId").innerHTML = globalBrokerId;
    } else {
        document.getElementById("outBrokerId").innerHTML = "Not defined";
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



async function registerBroker() {
    let company = document.getElementById("regCompany").value;
    let username = document.getElementById("regUsername").value;
    let password = document.getElementById("regPassword").value;

    let broker = {
        "company": company,
        "username": username,
        "password": password
    }

    try {
        const response = await fetch("http://localhost:8080/broker",
            {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(broker),
            });

        const result = await response.json();
        if( !response.ok )
        {
            const errorData = await response.json();
            showMessage('assetsMessage', errorData.message, 'error');
            throw new Error("Error: createBroker " + result.status );
        }

        // Setzte globale Variablen
        globalBrokerId = result.id;
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



async function confirmDeleteBroker() {
    const confirmation = confirm("Are you sure you want to delete your account?");
    if (confirmation) {
        try {
            // Überprüfen, ob der Broker angemeldet ist
            if (!globalBrokerId || !globalAccessToken) {
                showMessage('accountMessage', 'Broker not logged in.', 'error');
                return;
            }



            // Wenn keine Assets vorhanden sind, Account löschen
            await deleteBroker();
        } catch (error) {
            console.error("Error confirming deletion:", error);
            showMessage('accountMessage', error.message, 'error');
        }
    }
}






async function deleteBroker()
{
    try {
        const response = await fetch(`http://localhost:8080/broker/` + globalBrokerId,
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
            globalBrokerId = null;
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






async function createAsset(assetName, assetKind) {
    const assetCreateDtoIn = {
        "name": assetName,
        "kind": assetKind
    };

    try {
        const response = await fetch(`http://localhost:8080/broker/${globalBrokerId}/assets`, {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalAccessToken.accessToken,
            },
            body: JSON.stringify(assetCreateDtoIn),
        });

        if (!response.ok) {
            const errorData = await response.json();
            showMessage('assetsMessage', errorData.message, 'error');
            throw new Error(errorData.message);
        }

        const result = await response.json();
        console.log('Created asset:', result);
        getAssetsByBrokerId();
        showMessage('assetsMessage', 'Asset successfully created!', 'success');

    } catch (error) {
        console.error('Error creating asset:', error);
        showStatusError(error);
        showMessage('assetsMessage', error.message, 'error');
    }
}



async function getAssetsByBrokerId() {
    try {
        const response = await fetch(`http://localhost:8080/broker/${globalBrokerId}/assets`, {
            method: 'get',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalAccessToken.accessToken,
            },
        });

        if (response.ok) {
            const assets = await response.json();
            showOwnAssets(assets);
        } else {
            const errorData = await response.json();
            showMessage('accountMessage', errorData.message, 'error');
            throw new Error(errorData.message);
        }


    } catch (error) {
        console.error('Error fetching assets:', error);
        showStatusError(error);
        showMessage('assetsMessage', error.message, 'error');
    }
}

function showOwnAssets(assets) {
    const ownAssetsList = document.getElementById("ownAssetsList");
    let html = '';
    assets.forEach(asset => {
        html += `<li>${asset.name} (${asset.kind}) <button class="deleteAssetBtn" data-assetid="${asset.id}">Delete</button></li>`;
    });
    ownAssetsList.innerHTML = html;

    // Event Listener für die Delete-Buttons der Assets registrieren
    const deleteAssetBtns = document.getElementsByClassName("deleteAssetBtn");
    Array.from(deleteAssetBtns).forEach(button => {
        button.addEventListener("click", function () {
            const assetId = button.getAttribute("data-assetid");
            deleteAsset(assetId);
        });
    });
}






async function deleteAsset(assetId) {
    try {
        // Überprüfung, ob das Asset in einem Portfolio enthalten ist
        const isInPortfolio = await fetch(`http://localhost:8080/broker/assets/${assetId}/in-portfolio`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalAccessToken.accessToken,
            }
        }).then(response => response.json());

        if (isInPortfolio) {
            showMessage('assetsMessage', 'Asset cannot be deleted because it is part of a portfolio.', 'error');
            return;
        }

        const response = await fetch(`http://localhost:8080/broker/${globalBrokerId}/assets/${assetId}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalAccessToken.accessToken,
            }
        });

        if (!response.ok) {
            const errorData = await response.json();
            showMessage('assetsMessage', errorData.message, 'error');
            throw new Error(errorData.message);
        }

        console.log('Asset deleted successfully');
        getAssetsByBrokerId(); // Aktualisiere die Liste der Assets nach dem Löschen
        showMessage('assetsMessage', 'Asset successfully deleted!', 'success');

    } catch (error) {
        console.error('Error deleting asset:', error);
        showMessage('assetsMessage', error.message, 'error');
    }
}









function clearAll() {
    const errorElement = document.getElementById("error");
    const outAlleBrokerElement = document.getElementById("outAlleBroker");

    if (errorElement) errorElement.innerHTML = "";
    if (outAlleBrokerElement) outAlleBrokerElement.innerHTML = "";

    const loginUsername = document.getElementById("loginUsername");
    const loginPassword = document.getElementById("loginPassword");
    if (loginUsername) loginUsername.value = "";
    if (loginPassword) loginPassword.value = "";


    const regCompany = document.getElementById("regCompany");
    const regUsername = document.getElementById("regUsername");
    const regPassword = document.getElementById("regPassword");


    if (regCompany) regCompany.value = "";
    if (regUsername) regUsername.value = "";
    if (regPassword) regPassword.value = "";


    const updateCompany = document.getElementById("updateCompany");
    const updatePassword = document.getElementById("updatePassword");


    if (updateCompany) updateCompany.value = "";
    if (updatePassword) updatePassword.value = "";


    const assetName = document.getElementById("assetName");
    const assetKind = document.getElementById("assetKind").value;

    if (assetName) assetName.value = "";
    if (assetKind) assetKind.value = "";

    ownAssetsList.innerHTML = "";
}

function showStatusError(result) {
    document.getElementById("error").innerHTML = "Statuscode " + result.status + " : " + result.message;
}








function toggleSections() {
    const loginSection = document.getElementById("loginSection");
    const registerSection = document.getElementById("registerSection");
    const accountSection = document.getElementById("accountSection");
    const assetsSection = document.getElementById("assetsSection");

    if (globalBrokerId) {
        loginSection.classList.add("hidden");
        registerSection.classList.add("hidden");
        accountSection.classList.remove("hidden");
        assetsSection.classList.remove("hidden");
    } else {
        loginSection.classList.remove("hidden");
        registerSection.classList.remove("hidden");
        accountSection.classList.add("hidden");
        assetsSection.classList.add("hidden");
    }
}




