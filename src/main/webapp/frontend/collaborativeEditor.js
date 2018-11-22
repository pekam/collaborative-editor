window.configureCollaborativeEditor = element => {

  console.log(element);
  element.addEventListener('input', e => {

    console.log(e);

    checkCaretPos();

    // TODO: Support all input types.
    // Currently it works only with one char insert and delete
    switch(e.inputType) {
      case 'insertText':
        insertOperation(element._caretPos - 1, e.data);
        break;
      case 'deleteContentBackward':
        deleteOperation(element._caretPos);
        break;
    }
  });

  const insertOperation = (index, char) => {
    const operation = `insert[${index},${char}]`;
    send(operation);
  }

  const deleteOperation = index => {
    const operation = `delete[${index}]`;
    send(operation);
  }

  const send = operation => {
    console.log(operation);
    const event = new CustomEvent('operation', {detail: operation});
    element.dispatchEvent(event);
  }

  element.addEventListener('keydown', e => {
    checkCaretPos();
  });

  element._caretPos;
  const checkCaretPos = () => {
    const newPos = element.selectionStart;
    if (newPos !== element._caretPos) {
      element._caretPos = newPos;
      const event = new CustomEvent('caret-move', {detail: element._caretPos});
      element.dispatchEvent(event);
    }
  }

}
