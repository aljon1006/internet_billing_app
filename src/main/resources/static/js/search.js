// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('customerSearch');
    
    if (searchInput) {
        searchInput.addEventListener('input', function(e) {
            const query = e.target.value;
            
            if (query.length >= 2 || query.length === 0) {
                fetch(`/dashboard/data?query=${encodeURIComponent(query)}`)
                    .then(response => response.text())
                    .then(html => {
                        const tableBody = document.getElementById('customerTableContainer');
                        if (tableBody) {
                            tableBody.innerHTML = html;
                        }
                    })
                    .catch(err => console.warn('Search failed:', err));
            }
        });
    }
});