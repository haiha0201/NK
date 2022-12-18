import requests
import numpy as np
from PIL import Image
from io import BytesIO


class BadRequest(Exception):
    pass


def get_image_from_url(url):
    try:
        response = requests.get(url)
        img = np.array(Image.open(BytesIO(response.content)))
        if len(img.shape) == 2:
            img = np.stack([img, img, img], -1)
        assert len(img.shape) == 3
        return img
    except:
        raise BadRequest


def sort_text(items):
    w = sorted(items, key=lambda item:np.mean(item['box'][1::2])) # sort by y
    n = len(w)
    taken = [False for i in range(n)]
    lines = []
    for i in range(n):
        if taken[i]:
            continue
        y = np.mean(w[i]['box'][1::2])
        row = []
        for j in range(n):
            ymin = np.min(w[j]['box'][1::2])
            ymax = np.max(w[j]['box'][1::2])
            if not taken[j] and ymin <= y <= ymax:
                taken[j] = True
                row.append(w[j])

        row.sort(key=lambda item: np.mean(item['box'][::2])) # sort row by x
        lines.append([item['text'] for item in row])
    
    text = '\n'.join([' '.join(line) for line in lines])
    return text