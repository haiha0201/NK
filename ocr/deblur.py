import os
from time import time
from icecream import ic, install

import torch

from NAFNet.basicsr.models import create_model
from NAFNet.basicsr.utils import img2tensor as _img2tensor, tensor2img, imwrite
from NAFNet.basicsr.utils.options import parse
import numpy as np
import cv2
import matplotlib.pyplot as plt


def img2tensor(img, bgr2rgb=False, float32=True):
    img = img.astype(np.float32) / 255.
    return _img2tensor(img, bgr2rgb=bgr2rgb, float32=float32)


def deblur_image(model, img):
    img = img2tensor(img)
    with torch.no_grad():
        model.feed_data(data={'lq': img.unsqueeze(dim=0)})
        if model.opt['val'].get('grids', False):
            model.grids()
        model.test()
        if model.opt['val'].get('grids', False):
            model.grids_inverse()

        visuals = model.get_current_visuals()
        return tensor2img([visuals['result']], rgb2bgr=False)


def get_deblur_model():
    opt = parse('NAFNet/options/test/REDS/NAFNet-width64.yml', is_train=False)
    opt['num_gpu'] = 1
    opt['dist'] = False

    model_path = opt['path']['pretrain_network_g']
    model_dir = os.path.dirname(model_path)
    if not os.path.isfile(model_path):
        print('Download pretrained deblurring model')
        os.makedirs(model_dir, exist_ok=True)

        import gdown
        gdown.download(
            id='14D4V4raNYIOhETfcuuLI3bGLB-OYIv6X',
            output=model_path,
            quiet=True
        )

    return create_model(opt)


get_deblur_model()