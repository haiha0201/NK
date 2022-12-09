import requests
import numpy as np
from PIL import Image
from io import BytesIO


def get_image_from_url(url):
    response = requests.get(url)
    return np.array(Image.open(BytesIO(response.content)))