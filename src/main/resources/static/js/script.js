// Keep track of the active sort selection securely in global execution memory
let currentSortValue = "lastName,asc"; 

function changeDashboardPage(targetPageIndex) {
    const searchInput = document.getElementById("customerSearch");
    const activeSearch = searchInput ? searchInput.value : "";
    
    const encodedSearch = encodeURIComponent(activeSearch);
    const encodedSort = encodeURIComponent(currentSortValue);
    
    // Update browser navigation and context variables
    const newUrl = `/dashboard?page=${targetPageIndex}&searchQuery=${encodedSearch}&sortValue=${encodedSort}`;
    history.pushState({ page: targetPageIndex }, "", newUrl);

    // Call Spring Controller matching the verified parameter query names
    fetch(`/dashboard/data?searchQuery=${encodedSearch}&sortValue=${encodedSort}&page=${targetPageIndex}`)
        .then(response => {
            if (!response.ok) throw new Error("Failed to load backend pagination slice.");
            return response.text();
        })
        .then(htmlFragment => {
            const tableContainer = document.getElementById("customerTableContainer");
            if (tableContainer) {
                // Safely swap out pagination tabs and data loops without touching text entries
                tableContainer.innerHTML = htmlFragment;
            }
        })
        .catch(error => console.error("Data pipeline refresh tracking crash:", error));
}

// Attach application bindings exactly once on standard context initialization
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById("customerSearch");
    const sortOptions = document.querySelectorAll('.sort-option');
    const paymentSearchInput = document.getElementById("billSearch");

    if (paymentSearchInput) {
        paymentSearchInput.addEventListener('input', function(e) {
            const query = e.target.value;
            // Only search if empty or at least 2 characters are entered
            if (query.length >= 2 || query.length === 0) {
                changeBillingPage(0); // Snap back to first page slice when filters update
            }  
        });
    }

    if (searchInput) {
        searchInput.addEventListener('input', function(e) {
            const query = e.target.value;
            // Only search if empty or at least 2 characters are entered
            if (query.length >= 2 || query.length === 0) {
                changeDashboardPage(0); // Snap back to first page slice when filters update
            }
        });
    }

    sortOptions.forEach(option => {
        option.addEventListener('click', function(event) {
            event.preventDefault();

            currentSortValue = this.getAttribute('data-sort'); 
            const selectedText = this.querySelector('span').innerText;
            
            const dropdownButton = document.querySelector('.dropdown-toggle');
            if (dropdownButton) {
                dropdownButton.innerHTML = `<i class="bi bi-funnel me-1"></i> ${selectedText}`;
            }

            changeDashboardPage(0); // Snap back to page slice 0 on sorting updates
        });
    });

    // Handle deep linking url states on cold navigation loads
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('sortValue')) {
        currentSortValue = urlParams.get('sortValue');
        const activeOption = document.querySelector(`.sort-option[data-sort="${currentSortValue}"]`);
        if (activeOption) {
            const dropdownButton = document.querySelector('.dropdown-toggle');
            if (dropdownButton) {
                dropdownButton.innerHTML = `<i class="bi bi-funnel me-1"></i> ${activeOption.querySelector('span').innerText}`;
            }
        }
    }
});


/** Billing */
function changeBillingPage(targetPageIndex) {
    const searchInput = document.getElementById("billSearch");
    const activeSearch = searchInput ? searchInput.value : "";
    
    const encodedSearch = encodeURIComponent(activeSearch);
    
    // Update browser navigation and context variables
    const newUrl = `/billing?page=${targetPageIndex}&searchQuery=${encodedSearch}`;
    history.pushState({ page: targetPageIndex }, "", newUrl);

    // Call Spring Controller matching the verified parameter query names
    fetch(`/billing/data?searchQuery=${encodedSearch}&page=${targetPageIndex}`)
        .then(response => {
            if (!response.ok) throw new Error("Failed to load backend pagination slice.");
            return response.text();
        })
        .then(htmlFragment => {
            const contentDiv = document.getElementById("paymentLogsTableContainer");
            if (contentDiv) {
                contentDiv.innerHTML = htmlFragment; 
            }
        })
        .catch(error => console.error("Data pipeline refresh tracking crash:", error));
}