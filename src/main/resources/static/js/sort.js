document.addEventListener("DOMContentLoaded", function() {
    const sortOptions = document.querySelectorAll('.sort-option');

    sortOptions.forEach(option => {
        option.addEventListener('click', function(event) {
            event.preventDefault(); // Stop the page from jump-scrolling or reloading

            const selectedText = this.querySelector('span').innerText;
            const selectedSortValue = this.getAttribute('data-sort'); 
            console.log("Selected Label:", selectedText);       
            
            
            // Update the button UI text to reflect what is active
            const dropdownButton = document.querySelector('.dropdown-toggle');
            dropdownButton.innerHTML = `<i class="bi bi-funnel me-1"></i> ${selectedText}`;

            executeFilteredSearch(selectedSortValue);
        });
    });
});

// Example function to handle the actual API or routing process
function executeFilteredSearch(sortValue) {
    console.log("Selected Value:", sortValue); 
    fetch(`/dashboard/data?sortValue=${encodeURIComponent(sortValue)}`)
        .then(response => response.text())
        .then(html => {
            const tableBody = document.getElementById('customerTableContainer');
            if (tableBody) {
                tableBody.innerHTML = html;
            }
    })
    .catch(err => console.warn('Sorting failed:', err));
}