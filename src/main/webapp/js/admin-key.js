function showKeyDetail(id, userId, algo, pubKey, createdAt, revokedAt, status) {
    document.getElementById('detailId').innerText = "#" + id;
    document.getElementById('detailUserId').innerText = "User #" + userId;
    document.getElementById('detailAlgo').innerText = algo;
    document.getElementById('detailCreatedAt').innerText = createdAt;
    document.getElementById('detailRevokedAt').innerText = revokedAt;
    document.getElementById('detailPublicKey').value = pubKey;

    const statusEl = document.getElementById('detailStatus');
    statusEl.innerText = status;
    if(status === 'ACTIVE') {
        statusEl.className = "badge badge-active";
        statusEl.innerText = "Đang hoạt động (ACTIVE)";
    } else {
        statusEl.className = "badge badge-revoked";
    }

    document.getElementById('keyDetailModal').style.display = 'flex';
}

function closeDetailModal() {
    document.getElementById('keyDetailModal').style.display = 'none';
}
