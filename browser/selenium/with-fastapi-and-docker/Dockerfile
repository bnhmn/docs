FROM seleniarm/standalone-chromium:121.0

# Turn off python output buffering
ENV PYTHONUNBUFFERED=1

ENV PYTHON_VENV="/opt/venv"
ENV PATH="$PYTHON_VENV/bin:$PATH"

# Create Python virtual environment
RUN sudo apt-get update && \
    sudo apt-get install -y --no-install-recommends python3-venv && \
    sudo apt-get clean && \
    sudo rm -rf /var/lib/apt/lists/* && \
    sudo mkdir "$PYTHON_VENV" &&  \
    sudo chown seluser "$PYTHON_VENV" && \
    python3 -m venv "$PYTHON_VENV"

# Install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY *.py ./

ENV HEADLESS_MODE="true"
ENV PORT="8000"
CMD ["bash", "-c", "/opt/bin/entry_point.sh & uvicorn app:app --host 0.0.0.0 --port \"$PORT\""]
